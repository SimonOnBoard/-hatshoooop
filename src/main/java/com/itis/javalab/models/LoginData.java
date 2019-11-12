package com.itis.javalab.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;
@RequiredArgsConstructor
@Data
public class LoginData {
    private Map<String,String> loginParams;
}
