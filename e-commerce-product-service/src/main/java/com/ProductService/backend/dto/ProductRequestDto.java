package com.ProductService.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String productName;

    private long stockQuantity;

    private boolean isAvailable;

    private double productPrice;

    private Long categoryId;
}
