package com.ProductService.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequestDto {

    @NotEmpty(message = "ProductQuantityDto cannot be empty")
    @Valid
    private List<ProductQuantityDto> productQuantityDto;

    @NotNull(message = "userId cannot be null")
    private Long userId;

}
