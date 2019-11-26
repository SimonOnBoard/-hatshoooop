package com.itis.javalab.dao;

import com.itis.javalab.context.Component;
import com.itis.javalab.dto.AuthDataDTO;

import java.util.Optional;

@Component
public interface AuthDTODao extends CrudDao<AuthDataDTO> {
    Optional<AuthDataDTO> findByUserId(Long id);
    Optional<AuthDataDTO> findByName(String login);
}
