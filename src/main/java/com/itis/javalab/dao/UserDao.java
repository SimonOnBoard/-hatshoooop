package com.itis.javalab.dao;



import com.itis.javalab.context.Component;
import com.itis.javalab.dto.AuthDataDTO;
import com.itis.javalab.models.User;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public interface UserDao extends CrudDao<User>{
    Optional<User> findByName(String login);
    Optional<User> findByDTO(AuthDataDTO dto);
    Map<Long,String> findNamesByIds(Set<Long> ids);
    Double getBalance(Long id);
    void updateBalance(Long id, Double balance);
}
