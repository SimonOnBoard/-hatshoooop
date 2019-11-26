package com.itis.javalab.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ProductDTO {
    private String name;
    private Integer count;
    private Long dateTime;
}
