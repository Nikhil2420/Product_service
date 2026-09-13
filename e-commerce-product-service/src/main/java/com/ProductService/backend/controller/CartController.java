package com.ProductService.backend.controller;

import com.ProductService.backend.dto.CartRequestDto;
import com.ProductService.backend.dto.CartResponseDto;
import com.ProductService.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addProductToCart(@RequestBody @Valid CartRequestDto cartRequestDto){
        CartResponseDto cartResponseDto=cartService.addProductToCart(cartRequestDto);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.CREATED);
    }



}
