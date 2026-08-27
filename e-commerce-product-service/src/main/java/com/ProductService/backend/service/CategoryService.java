package com.ProductService.backend.service;

import com.ProductService.backend.dto.CategoryRequestDto;
import com.ProductService.backend.dto.CategoryResponseDto;
import com.ProductService.backend.entity.Category;
import com.ProductService.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = Category.builder().categoryName(categoryRequestDto.getCategoryName()).build();
        categoryRepository.save(category);

        return CategoryResponseDto.builder().CategoryName(category.getCategoryName()).build();

    }
}
