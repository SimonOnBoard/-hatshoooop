package com.itis.javalab.service;

import com.itis.javalab.dao.MessageDao;
import com.itis.javalab.dao.MessageDaoImpl;
import com.itis.javalab.dao.UserDao;
import com.itis.javalab.dao.UserDaoImpl;
import com.itis.javalab.models.Message;
import com.itis.javalab.dto.MessageDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class MessageDTOTranslator {
    private static MessageDao messageDao = null;
    private static UserDao userDao = null;
    public static void loadConfig(String[] properties) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(properties[0], properties[1], properties[2]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        messageDao = new MessageDaoImpl(connection);
        userDao = new UserDaoImpl(connection);
    }

    public static String getMessagesViaPagination(JsonWorker transfer) {
        Long page = Long.parseLong(transfer.getPayloadParam("number"));
        Long size = Long.parseLong(transfer.getPayloadParam("size"));
        List<Message> messages = getListMessages(page, size, messageDao);
        if (messages.size() == 0) {
            return transfer.prepareCustomEmptyListMessage();
        }
        Set<Long> ids = new HashSet<>();
        messages.stream().forEach(message -> ids.add(message.getOwnerId()));
        Map<Long, String> names = getUsersNames(userDao, ids);
        List<MessageDTO> listMessageDTO = new ArrayList<>();
        getListOfDTO(names,messages, listMessageDTO);
        return transfer.getPreparedListOfMessages(listMessageDTO);
    }

    private static void getListOfDTO(Map<Long, String> names, List<Message> messages, List<MessageDTO> listMessageDTO) {
        messages.stream().forEach(
                message -> {
                    listMessageDTO.add(new MessageDTO(message.getId(),message.getText(),names.get(message.getOwnerId()),
                            (Long) Timestamp.valueOf(message.getDateTime()).getTime()));
                }
        );
    }

    private static Map<Long, String> getUsersNames(UserDao userDao, Set<Long> ids) {
        return userDao.findNamesByIds(ids);
    }

    private static List<Message> getListMessages(Long page, Long size, MessageDao messageDao) {
        return messageDao.findMessagesOnPage(size, (page - 1 ) * size);
    }
}
