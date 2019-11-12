package com.itis.javalab.dao;

import com.itis.javalab.models.Message;
import com.itis.javalab.models.Product;
import com.itis.javalab.models.ProductDTO;
import com.itis.javalab.models.User;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends CrudDao<Product>{
    Optional<Product> findByName(String name);
    void savePaymentAct(Long userId, Long productId, Integer count);
    List<Product> findProductsOnPage(Long limit, Long offset);
}
