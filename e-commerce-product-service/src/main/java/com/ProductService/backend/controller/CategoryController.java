package com.ProductService.backend.controller;

import com.ProductService.backend.dto.CategoryRequestDto;
import com.ProductService.backend.dto.CategoryResponseDto;
import com.ProductService.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody CategoryRequestDto categoryRequestDto){

        CategoryResponseDto responseDto=categoryService.addCategory(categoryRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
