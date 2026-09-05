package com.ProductService.backend.service;

import com.ProductService.backend.dto.ProductRequestDto;
import com.ProductService.backend.dto.ProductResponseDto;
import com.ProductService.backend.entity.Category;
import com.ProductService.backend.entity.Product;
import com.ProductService.backend.repository.CategoryRepository;
import com.ProductService.backend.repository.ProductRepository;
import com.ProductService.backend.utility.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException(
                                "No category found for the categoryId" + ":" + productRequestDto.getCategoryId()
                        )
                );

        Product product = Product.builder().productName(productRequestDto.getProductName())
                .stockQuantity(productRequestDto.getStockQuantity())
                .isAvailable(productRequestDto.isAvailable())
                .productPrice(productRequestDto.getProductPrice())
                .category(category)
                .build();
        /*
            To keep both Java objects synchronized but because of
            mappedBy = "category" tells Hibernate:
            "Look at the category field in Product to find which products belong to this Category."
            when we do category.getProductlist()
         */
        // category.addProduct(product);
        productRepository.save(product);
        return ProductMapper.mapProductToProductResponseDto(product);

    }

    public List<ProductResponseDto> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = products.stream()
                .map(product -> {
                    return ProductMapper.mapProductToProductResponseDto(product);
                }).toList();
        return productResponseDtos;
    }

    public ProductResponseDto getProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return ProductMapper.mapProductToProductResponseDto(product.get());
        }
        throw new RuntimeException("No product found for this productId" + ":" + productId);
    }

    public ProductResponseDto updateProduct(ProductRequestDto productRequestDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("No product found for this productId" + ":" + productId));
        product.setProductPrice(productRequestDto.getProductPrice());
        product.setProductName(productRequestDto.getProductName());
        product.setStockQuantity(productRequestDto.getStockQuantity());
        product.setAvailable(productRequestDto.isAvailable());
        productRepository.save(product);
        return ProductMapper.mapProductToProductResponseDto(product);
    }

    public ProductResponseDto deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No product found for this productId" + ":" + id));


        /*
            product.getCategory().removeProduct(product);this is not required but
            it is better to do this for java object synchronization
         */
        /* Hibernate do this when we do category.getProductList()
            SELECT *
            FROM product_table
            WHERE category_id = ?;
            Since the deleted product is no longer
            in product_table,
            Hibernate creates the newly loaded productList without that product
         */
        productRepository.deleteById(id);
        return ProductMapper.mapProductToProductResponseDto(product);
    }
}
