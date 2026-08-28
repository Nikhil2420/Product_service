package com.ProductService.backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDto {

    private String productName;
    private double productPrice;

}
