package com.itis.javalab.dao;

import com.itis.javalab.models.AuthDataDTO;

import java.util.Optional;

public interface AuthDTODao extends CrudDao<AuthDataDTO> {
    Optional<AuthDataDTO> findByUserId(Long id);
    Optional<AuthDataDTO> findByName(String login);
}
