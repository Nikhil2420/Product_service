package com.ProductService.backend.service;

import com.ProductService.backend.dto.CategoryRequestDto;
import com.ProductService.backend.dto.CategoryResponseDto;
import com.ProductService.backend.dto.PaginationResponseDto;
import com.ProductService.backend.entity.Category;
import com.ProductService.backend.exception.CategoryNotFoundException;
import com.ProductService.backend.repository.CategoryRepository;
import com.ProductService.backend.utility.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /*
     * Pageable = client request (page, size, sort) request format
     * page     = which page to fetch (starts from 0) how the response looks
     * size     = records per page
     * offset   = page × size → records to skip when db generate sql query
     * limit    = size → records to return
     * Page     = application response (data(db response) + pagination metadata(like page,size,offset))
     */
    public PaginationResponseDto<CategoryResponseDto> getAllCategory(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        Page<CategoryResponseDto> categoryResponseDtos= categories
                .map(category -> {
                    return CategoryResponseDto.builder()
                            .CategoryName(category.getCategoryName())
                            .productDtoList(ProductMapper.mapProductToProductDto(category.getProductList()))
                            .build();
                });
        return PaginationResponseDto.<CategoryResponseDto>builder()
                .content(categoryResponseDtos.getContent())
                .pageNumber(categoryResponseDtos.getNumber())
                .pageSize(categoryResponseDtos.getSize())
                .totalElement(categoryResponseDtos.getTotalElements())
                .totalPage(categoryResponseDtos.getTotalPages())
                .first(categoryResponseDtos.isFirst())
                .last(categoryResponseDtos.isLast())
                .empty(categoryResponseDtos.isEmpty())
                .build();
    }


    public CategoryResponseDto deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("No Category found for this id" + " : " + id)
                );
        /*
            we have to delete related  product also that belong to this category;
            other foreign key violation error we will get
            added cascade on category
        */

        categoryRepository.deleteById(id);

        return CategoryResponseDto.builder()
                .CategoryName(category.getCategoryName())
                .productDtoList(ProductMapper.mapProductToProductDto(category.getProductList()))
                .build();
    }

    public CategoryResponseDto updateCategoryName(String categoryName, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("No category found for this categoryId" + " : " + categoryId));

        category.setCategoryName(categoryName);
        categoryRepository.save(category);
        return CategoryResponseDto.builder()
                .CategoryName(categoryName)
                .productDtoList(ProductMapper.mapProductToProductDto(category.getProductList()))
                .build();
    }
}
