package com.ProductService.backend.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private String CategoryName;
    private List<ProductDto> productDtoList;
}
