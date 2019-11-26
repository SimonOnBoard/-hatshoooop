package com.itis.javalab.dao;

import com.itis.javalab.dto.AuthDataDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserRowMapper<T> {
    T mapRow(ResultSet row, AuthDataDTO dataDTO) throws SQLException;
}
