package com.itis.javalab.service;

import com.itis.javalab.dao.MessageDaoImpl;
import com.itis.javalab.dao.UserDao;
import com.itis.javalab.dao.UserDaoImpl;
import com.sun.jmx.mbeanserver.NamedObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BalanceService {
    public static UserDao userDao = null;
    public static void loadConfig(String[] properties) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(properties[0], properties[1], properties[2]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        userDao = new UserDaoImpl(connection);

    }
    public static Double getBalance(JsonWorker jsonWorker){
        jsonWorker.checkJWT();
        Long id = jsonWorker.jwt.getClaim("id").asLong();
        return userDao.getBalance(id);
    }
    public static void updateBalance(JsonWorker jsonWorker, Double balance){
        jsonWorker.checkJWT();
        Long id = jsonWorker.jwt.getClaim("id").asLong();
        userDao.updateBalance(id,balance);
    }

    public static boolean checkAvaliableBalance(JsonWorker jsonWorker, Double price, Integer count) {
        Double balance = getBalance(jsonWorker);
        Double amount = price * count;
        return balance >= amount;
    }

    public static void setBalance(JsonWorker jsonWorker, Double price, Integer count) {
        Double balance = getBalance(jsonWorker);
        balance = balance - price * count;
        updateBalance(jsonWorker,balance);
    }
}
