package com.ProductService.backend.service;

import com.ProductService.backend.dto.ProductRequestDto;
import com.ProductService.backend.dto.ProductResponseDto;
import com.ProductService.backend.entity.Category;
import com.ProductService.backend.entity.Product;
import com.ProductService.backend.repository.CategoryRepository;
import com.ProductService.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Optional<Category> category = categoryRepository.findById(productRequestDto.getCategoryId());
        Category category1=null;
        if(category.isPresent()){
            category1=category.get();
        }
        Product product = Product.builder().productName(productRequestDto.getProductName())
                .stockQuantity(productRequestDto.getStockQuantity())
                .isAvailable(productRequestDto.isAvailable())
                .productPrice(productRequestDto.getProductPrice())
                .category(category1)
                .build();
        productRepository.save(product);
        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.isAvailable())
                .build();
        return productResponseDto;

    }
}
