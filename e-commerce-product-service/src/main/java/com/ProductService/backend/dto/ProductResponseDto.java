package com.ProductService.backend.dto;

import lombok.Builder;

@Builder
public class ProductResponseDto {

    private String productName;
    private long stockQuantity;
    private boolean isAvailable;
    private double productPrice;

}
