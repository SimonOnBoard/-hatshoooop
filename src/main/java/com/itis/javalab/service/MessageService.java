package com.itis.javalab.service;

import com.itis.javalab.dao.*;
import com.itis.javalab.models.Message;
import com.itis.javalab.servers.ChatMultiServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageService {
    private static UserDao userDao;
    private static MessageDao messageDao;
    private static Connection connection;

    public static void loadConfig(String[] properties) {
        try {
            connection = DriverManager.getConnection(properties[0], properties[1], properties[2]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        messageDao = new MessageDaoImpl(connection);
        userDao = new UserDaoImpl(connection);

    }

    public static String getMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        saveMessage(message, now);
        String resultMessage = constructResultString(message, now);
        return resultMessage;
    }

    private static String constructResultString(String message, LocalDateTime now) {
        if (message.equals(".")) {
            return "" + JsonWorker.jwt.getClaim("nickName").asString() + " on " + now.toString() + " : " + "Bye";
        }
        return "" + JsonWorker.jwt.getClaim("nickName").asString() + " on " + now.toString() + " : " + message;
    }

    private static void saveMessage(String message, LocalDateTime now) {
        messageDao.save(new Message(message, now, JsonWorker.jwt.getClaim("id").asLong()));
    }

    public static void sendMessage(JsonWorker jsonWorker, List<ChatMultiServer.ClientHandler> clients ){
        jsonWorker.checkJWT();
        String message = MessageService.getMessage(jsonWorker.getMessage());
        String jsonToSend = jsonWorker.prepareMessage(message);
        SenderService.send((CopyOnWriteArrayList<ChatMultiServer.ClientHandler>) clients, jsonToSend);
    }
}
