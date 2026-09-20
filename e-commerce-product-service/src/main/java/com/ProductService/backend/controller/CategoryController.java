package com.ProductService.backend.controller;

import com.ProductService.backend.dto.CategoryRequestDto;
import com.ProductService.backend.dto.CategoryResponseDto;
import com.ProductService.backend.dto.PaginationResponseDto;
import com.ProductService.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {

        CategoryResponseDto responseDto = categoryService.addCategory(categoryRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<PaginationResponseDto<CategoryResponseDto>> getCategory(Pageable pageable) {
        PaginationResponseDto<CategoryResponseDto> categoryResponseDtos = categoryService.getAllCategory(pageable);
        return new ResponseEntity<>(categoryResponseDtos, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable(name = "categoryId") Long id) {
        CategoryResponseDto categoryResponseDto = categoryService.deleteCategory(id);
        return new ResponseEntity<>(categoryResponseDto, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategoryName(@RequestParam String categoryName,
                                                              @PathVariable Long categoryId){
        CategoryResponseDto categoryResponseDto=categoryService.updateCategoryName(categoryName,categoryId);
        return new ResponseEntity<>(categoryResponseDto,HttpStatus.ACCEPTED);
    }
}
