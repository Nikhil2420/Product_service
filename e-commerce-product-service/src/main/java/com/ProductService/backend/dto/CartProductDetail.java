package com.ProductService.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDetail {

    private String productName;
    private double productPrice;
    private long productQuantity;
}
