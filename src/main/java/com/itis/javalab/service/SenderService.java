package com.itis.javalab.service;

import com.itis.javalab.servers.ChatMultiServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;

public class SenderService {
    public static synchronized void send(CopyOnWriteArrayList<ChatMultiServer.ClientHandler> clients, String message) {
        for (ChatMultiServer.ClientHandler client : clients) {
            try {
                PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    public static void sendToCurrentSocket(PrintWriter out, String message){
        out.println(message);
    }
}
