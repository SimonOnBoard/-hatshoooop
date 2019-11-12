package com.itis.javalab.service;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.JWT;

import static com.itis.javalab.service.TokenCreator.algorithm;

public class TokenVerifyHelper {
    public static DecodedJWT verify(String token){
        DecodedJWT jwt;

        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .build();
            jwt = jwtVerifier.verify(token);

        } catch (JWTVerificationException e) {
            throw new IllegalStateException(e);
        }
        return jwt;
    }
}
