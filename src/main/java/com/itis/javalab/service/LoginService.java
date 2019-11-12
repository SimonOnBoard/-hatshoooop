package com.itis.javalab.service;

import com.itis.javalab.dao.AuthDTODao;
import com.itis.javalab.dao.AuthDTODaoImpl;
import com.itis.javalab.dao.UserDao;
import com.itis.javalab.dao.UserDaoImpl;
import com.itis.javalab.models.AuthDataDTO;
import com.itis.javalab.models.LoginData;
import com.itis.javalab.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class LoginService {
    private static Connection connection = null;
    private static AuthDTODao authDTODao = null;
    private static PasswordEncoder encoder = null;
    private static UserDao userDao = null;
    public synchronized static String checkLogin(LoginData data) {
        Optional<AuthDataDTO> authData = authDTODao.findByName(data.getLoginParams().get("login"));
        if (authData.isPresent()) {
            if (encoder.matches(data.getLoginParams().get("password"), authData.get().getPassword())) {
                Optional<User> user = userDao.findByDTO(authData.get());
                if (user.isPresent()) {
                    String token = TokenCreator.createToken(user.get());
                    return token;
                }
            }
        }
        return null;
    }

    public static void loadConfig(String[] properties) {
        encoder = new BCryptPasswordEncoder();
        try {
            connection = DriverManager.getConnection(properties[0],properties[1],properties[2]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        authDTODao = new AuthDTODaoImpl(connection);
        userDao = new UserDaoImpl(connection);
    }
}
