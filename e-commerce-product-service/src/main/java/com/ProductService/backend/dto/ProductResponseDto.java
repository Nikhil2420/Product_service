package com.ProductService.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
public class ProductResponseDto {

    private String productName;
    private long stockQuantity;

    /*
        @JsonProperty("isAvailable")
        tells Jackson exactly what name to use for that Java field when
        converting between Java object ↔ JSON.
     */
    @JsonProperty("isAvailable")
    private boolean available;

    private double productPrice;

}
