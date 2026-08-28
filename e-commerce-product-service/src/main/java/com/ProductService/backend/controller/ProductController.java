package com.ProductService.backend.controller;

import com.ProductService.backend.dto.ProductRequestDto;
import com.ProductService.backend.dto.ProductResponseDto;
import com.ProductService.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto productRequestDto) {

        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);
        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }


    @GetMapping("/getAllProduct")
    public ResponseEntity<List<ProductResponseDto>> getAllProduct() {
        List<ProductResponseDto> productResponseDtos = productService.getAllProducts();
        return new ResponseEntity<>(productResponseDtos, HttpStatus.FOUND);
    }

    @GetMapping("/productId/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") Long productId) {
        ProductResponseDto productResponseDto = productService.getProduct(productId);
        return new ResponseEntity<>(productResponseDto, HttpStatus.FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductRequestDto productRequestDto,
                                                            @RequestParam("productId") Long id) {
        ProductResponseDto productResponseDto = productService.updateProduct(productRequestDto,id);
        return new ResponseEntity<>(productResponseDto, HttpStatus.FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ProductResponseDto> deleteProduct(@RequestParam("productId") Long id){
        ProductResponseDto productResponseDto=productService.deleteProduct(id);
        return new ResponseEntity<>(productResponseDto,HttpStatus.ACCEPTED);
    }

}
