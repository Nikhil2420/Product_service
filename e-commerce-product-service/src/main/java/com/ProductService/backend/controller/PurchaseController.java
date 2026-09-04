package com.ProductService.backend.controller;

import com.ProductService.backend.dto.PurchaseRequestDto;
import com.ProductService.backend.dto.PurchaseResponseDto;
import com.ProductService.backend.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    @PostMapping("/product")
    public ResponseEntity<PurchaseResponseDto> buyProduct(@RequestBody @Valid PurchaseRequestDto purchaseRequestDto){
        PurchaseResponseDto purchaseResponseDto=purchaseService.buyProduct(purchaseRequestDto);
        return new ResponseEntity<>(purchaseResponseDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseResponseDto> getPurchaseById(@PathVariable Long purchaseId){
        PurchaseResponseDto purchaseResponseDto=purchaseService.getPurchaseById(purchaseId);
        return new ResponseEntity<>(purchaseResponseDto,HttpStatus.FOUND);
    }




}
