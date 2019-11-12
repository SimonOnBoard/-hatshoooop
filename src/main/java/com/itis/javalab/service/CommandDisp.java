package com.itis.javalab.service;

import com.itis.javalab.models.Product;

import java.io.PrintWriter;

public class CommandDisp {
    public static void getCurrentServise(PrintWriter out, JsonWorker jsonWorker) {
        jsonWorker.checkJWT();
        String command = jsonWorker.getPayloadParam("command");
        String jsonToWrite = null;
        switch (command){
            case "get messages":
                jsonToWrite = MessageDTOTranslator.getMessagesViaPagination(jsonWorker);
                break;
            case "get products":
                jsonToWrite = ProductService.getProductListViaPagination(jsonWorker);
                break;
            case "add product":
                if(jsonWorker.jwt.getClaim("role").asString().equals("ADMIN")){
                    jsonToWrite = ProductService.addnewProduct(jsonWorker);
                }
                else{
                    jsonToWrite = jsonWorker.prepareFailAuthMessage();
                }
                break;
            case "remove product":
                if(jsonWorker.jwt.getClaim("role").asString().equals("ADMIN")){
                    jsonToWrite = ProductService.removeProduct(jsonWorker);
                }
                else{
                    jsonToWrite = jsonWorker.prepareFailAuthMessage();
                }
                break;
            case "buy":
                jsonToWrite = ProductService.registrePayment(jsonWorker);
                break;
            default:
                System.out.println("another case");

        }
        out.println(jsonToWrite);
    }
}
