package com.itis.javalab.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.dto.MessageDTO;
import com.itis.javalab.dto.ShowProductDTO;
import com.itis.javalab.models.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWorker {
    private ObjectMapper objectMapper;
    private static JsonNode root = null;
    private static Map<String,String> header = null;
    private static Map<String,Object> payload = null;
    public static DecodedJWT jwt = null;
    public JsonWorker() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public void loadHeader() throws JsonProcessingException {
        this.header = objectMapper.convertValue(root.get("header"), new TypeReference<Map<String, String>>(){});
    }

    public String getHeaderParam(String parametr){
        return this.header.get(parametr);
    }
    public void loadMessage(String inputLine){
        try {
            this.root = objectMapper.readTree(inputLine);
            this.loadHeader();
            this.loadPayload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginData loadLoginData() {
        try {
            return objectMapper.treeToValue(root.get("payload"), LoginData.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String sendLoginAnswer(String token) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        if(token != null){
            header.put("typ","200L");
            header.put("bearer",token);
            params.put("message","Welcome");
            return getStringOfMessage(header, params);
        }
        return null;
    }


    public void checkJWT() {
        this.jwt = TokenVerifyHelper.verify(this.getHeaderParam("bearer"));
    }

    public String getMessage() {
        return (String) payload.get("message");
    }

    private void loadPayload() {
        payload = objectMapper.convertValue(root.get("payload"), new TypeReference<Map<String, Object>>(){});
    }

    public String prepareMessage(String message) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","message");
        params.put("message",message);
        return getStringOfMessage(header, params);
    }

    private String getStringOfMessage(Map<String, String> header, Map<String, Object> params) {
        Map<String, Object> message = new HashMap<>();
        message.put("header", header);
        message.put("payload", params);
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String sendLogout() {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","logout");
        params.put("message","Пока:)");
        return getStringOfMessage(header, params);
    }

    public String getPayloadParam(String command) {
        Object ob = payload.get(command);
        if(ob instanceof Number){
            return "" + ob.toString();
        }
        return (String) ob;
    }

    public String getPreparedListOfMessages(List<MessageDTO> listMessageDTO) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","200M");
        params.put("data",listMessageDTO);
        return getStringOfMessage(header, params);
    }

    public String prepareFailAuthMessage() {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","403");
        params.put("message","You are not an ADMIN, acc denied");
        return getStringOfMessage(header, params);
    }

    public Product readProduct() {
        try {
            return objectMapper.treeToValue(root.get("payload").get("product"), Product.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }
    }

    public String prepareCustomEmptyListMessage() {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","200M");
        header.put("emt","0");
        return getStringOfMessage(header, params);
    }

    public String prepareSuccessSaveMessage(String message) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","200S");
        params.put("message",message);
        return getStringOfMessage(header, params);
    }

    public ProductDTO readProductInfo() {
        try {
            return objectMapper.treeToValue(root.get("payload").get("product"), ProductDTO.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }
    }

    public String prepareCustomFailMessage(String message) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","300E");
        params.put("message",message);
        return getStringOfMessage(header, params);
    }

    public String preparePaymentSuccessMessage(ProductDTO info) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","300S");
        params.put("product",info);
        params.put("message","Покупка успешно совершена");
        return getStringOfMessage(header, params);
    }

    public String preparedListOfProducts(List<ShowProductDTO> products1) {
        Map<String,String> header = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        header.put("typ","200P");
        params.put("data",products1);
        return getStringOfMessage(header, params);
    }
}
