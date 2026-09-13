package com.ProductService.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDto {

    @NotNull(message = "productId cannot be null")
    private Long productId;
    @Min(value = 1,message = "at least the productQuantity should be 1")
    private int productQuantity;
}
