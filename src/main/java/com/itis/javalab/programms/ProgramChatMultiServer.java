package com.itis.javalab.programms;

import com.beust.jcommander.JCommander;
import com.itis.javalab.context.ApplicationContextReflectionBased;
import com.itis.javalab.dao.UserDaoImpl;
import com.itis.javalab.servers.ChatMultiServer;
import com.itis.javalab.service.Args;
import com.itis.javalab.service.PropertiesLoader;
import com.itis.javalab.context.ApplicationContext;

public class ProgramChatMultiServer {
    public static void main(String[] args) {
        Args args1 = new Args();
        JCommander jCommander = new JCommander(args1);
        jCommander.parse(args);
        String[] properties = PropertiesLoader.getProperties(args1.getPath_prop());
        ApplicationContext context = new ApplicationContextReflectionBased(properties);
        Object object = context.getComponent(UserDaoImpl.class,"UserDao");
        ChatMultiServer server = new ChatMultiServer(properties,context);
        server.start(Integer.parseInt(args1.getPort()));
    }
}
