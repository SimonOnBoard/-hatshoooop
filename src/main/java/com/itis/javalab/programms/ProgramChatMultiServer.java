package com.itis.javalab.programms;

import com.beust.jcommander.JCommander;
import com.itis.javalab.context.ApplicationContextReflectionBased;
import com.itis.javalab.context.ApplicationContextReflectionBasedSecondImpl;
import com.itis.javalab.context.interfaces.AnotherApplicationContext;
import com.itis.javalab.servers.ChatMultiServer;
import com.itis.javalab.services.programm.Args;
import com.itis.javalab.services.programm.PropertiesLoader;
import com.itis.javalab.context.interfaces.ApplicationContext;

public class ProgramChatMultiServer {
    public static void main(String[] args) {
        Args args1 = new Args();
        JCommander jCommander = new JCommander(args1);
        jCommander.parse(args);
        String[] properties = PropertiesLoader.getProperties(args1.getPath_prop());
        properties[4] = "com.itis.javalab";
        properties[5] = "com.itis.javalab.dao.interfaces.CrudDao";
        //ApplicationContext context = new ApplicationContextReflectionBased(properties);
        AnotherApplicationContext context1 = new ApplicationContextReflectionBasedSecondImpl(properties);
        ChatMultiServer server = new ChatMultiServer(context1);
        server.start(Integer.parseInt(args1.getPort()));
    }
}
