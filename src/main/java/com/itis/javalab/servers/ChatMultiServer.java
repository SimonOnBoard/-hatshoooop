package com.itis.javalab.servers;


import com.itis.javalab.context.ApplicationContext;
import com.itis.javalab.models.LoginData;
import com.itis.javalab.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatMultiServer {
    // список клиентов
    private List<ClientHandler> clients;
    private String[] properties;
    private Connection connection;

    public ChatMultiServer(String[] properties, ApplicationContext context) {
        this.connection = context.getComponent(Connection.class,"connection");
        this.properties = this.properties;
        LoginService.loadConfig(this.properties);
        MessageService.loadConfig(this.properties);
        MessageDTOTranslator.loadConfig(this.properties);
        ProductService.loadConfig(this.properties);
        BalanceService.loadConfig(this.properties);
        clients = new CopyOnWriteArrayList<>();
        try {
            this.connection = DriverManager.getConnection(this.properties[0], this.properties[1], this.properties[2]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void start(int port) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        while (true) {
            try {
                // запускаем обработчик сообщений для каждого подключаемого клиента
                new ClientHandler(serverSocket.accept()).start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public class ClientHandler extends Thread {
        // связь с одним клиентом
        public Socket clientSocket;
        private BufferedReader in;
        private JsonWorker jsonWorker;

        ClientHandler(Socket socket) {
            this.jsonWorker = new JsonWorker();
            this.clientSocket = socket;
        }

        public void run() {
            try {
                System.out.println("New user connection");
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonWorker.loadMessage(inputLine);
                    switch (jsonWorker.getHeaderParam("typ")) {
                        case "login":
                            LoginService.startLoginprocess(out, jsonWorker, this, clients);
                            break;
                        case "message":
                            MessageService.sendMessage(jsonWorker, clients);
                            break;
                        case "logout":
                            stopClientConnection(out);
                            break;
                        case "command":
                            CommandDisp.getCurrentServise(out, jsonWorker);
                            break;
                    }
                }
                remove();
                this.stopConnection();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private void remove() {
            if (clients.contains(this)) {
                clients.remove(this);
            }
        }

        private void stopClientConnection(PrintWriter out) {
            jsonWorker.checkJWT();
            String jsonToSend = jsonWorker.sendLogout();
            SenderService.sendToCurrentSocket(out, jsonToSend);
            clients.remove(this);
            try {
                this.stopConnection();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }


        private void stopConnection() throws IOException {
            this.clientSocket.close();
            in.close();
            this.stop();
        }
    }
}
