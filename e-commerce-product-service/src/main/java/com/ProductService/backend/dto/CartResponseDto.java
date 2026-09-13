package com.ProductService.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private List<CartProductDetail> cartProductDetailList;
    private double totalCartPrice;
    private int capacityLeftInCart;
}
