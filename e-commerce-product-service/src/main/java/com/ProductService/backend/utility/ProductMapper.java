package com.ProductService.backend.utility;

import com.ProductService.backend.dto.ProductDto;
import com.ProductService.backend.dto.ProductResponseDto;
import com.ProductService.backend.entity.Product;

import java.util.List;

public class ProductMapper {

    public static List<ProductDto> mapProductToProductDto(List<Product> products) {
        return products.stream()
                .map(product -> {
                    return ProductDto.builder()
                            .productName(product.getProductName())
                            .productPrice(product.getProductPrice())
                            .build();
                }).toList();
    }

    public static ProductResponseDto mapProductToProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .stockQuantity(product.getStockQuantity())
                .available(product.isAvailable())
                .build();
    }
}
