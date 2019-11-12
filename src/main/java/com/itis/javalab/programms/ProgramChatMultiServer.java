package com.itis.javalab.programms;

import com.beust.jcommander.JCommander;
import com.itis.javalab.servers.ChatMultiServer;
import com.itis.javalab.service.Args;
import com.itis.javalab.service.PropertiesLoader;


public class ProgramChatMultiServer {
    public static void main(String[] args) {
        Args args1 = new Args();
        JCommander jCommander = new JCommander(args1);
        jCommander.parse(args);
        String[] properties = PropertiesLoader.getProperties(args1.getPath_prop());
        ChatMultiServer server = new ChatMultiServer(properties);
        server.start(Integer.parseInt(args1.getPort()));
    }
}
