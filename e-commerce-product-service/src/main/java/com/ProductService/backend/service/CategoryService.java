package com.ProductService.backend.service;

import com.ProductService.backend.dto.CategoryRequestDto;
import com.ProductService.backend.dto.CategoryResponseDto;
import com.ProductService.backend.entity.Category;
import com.ProductService.backend.repository.CategoryRepository;
import com.ProductService.backend.utility.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = Category.builder().categoryName(categoryRequestDto.getCategoryName()).build();
        categoryRepository.save(category);

        return CategoryResponseDto.builder().CategoryName(category.getCategoryName()).build();

    }

    public List<CategoryResponseDto> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> {
                    return CategoryResponseDto.builder()
                            .CategoryName(category.getCategoryName())
                            .productDtoList(ProductMapper.mapProductToProductDto(category.getProductList()))
                            .build();
                }).toList();
    }


}
