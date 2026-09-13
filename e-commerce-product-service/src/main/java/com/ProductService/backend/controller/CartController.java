package com.ProductService.backend.controller;

import com.ProductService.backend.dto.*;
import com.ProductService.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addProductToCart(@RequestBody @Valid CartRequestDto cartRequestDto) {
        CartResponseDto cartResponseDto = cartService.addProductToCart(cartRequestDto);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/buy/products/{userId}")
    private ResponseEntity<BuyProductFromCartResponseDto> buyProductFromCart(@RequestBody @Valid BuyProductFromCartRequestDto buyProductFromCartRequestDto,
                                                                             @PathVariable @Positive Long userId) {

        BuyProductFromCartResponseDto buyProductFromCartResponseDto = cartService.buyProductFromCart(buyProductFromCartRequestDto,userId);
        return new ResponseEntity<>(buyProductFromCartResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/product/{userId}")
    public ResponseEntity<List<ProductDto>> getAllProductFromCart(@PathVariable Long userId){
        List<ProductDto> productDtoList=cartService.getAllProductFromCart(userId);
        return new ResponseEntity<>(productDtoList, HttpStatus.FOUND);
    }
}
