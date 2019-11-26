package com.itis.javalab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ShowProductDTO {
    private String name;
    private Double price;
    private Integer count;

    public ShowProductDTO(String name, Double price, Integer count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
