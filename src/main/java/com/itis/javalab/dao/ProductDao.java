package com.itis.javalab.dao;

import com.itis.javalab.context.Component;
import com.itis.javalab.models.Message;
import com.itis.javalab.models.Product;
import com.itis.javalab.models.ProductDTO;
import com.itis.javalab.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public interface ProductDao extends CrudDao<Product>{
    Optional<Product> findByName(String name);
    LocalDateTime savePaymentAct(Long userId, Long productId, Integer count);
    List<Product> findProductsOnPage(Long limit, Long offset);
    List<ProductDTO> findAllPaymentsById(Long id);
}
