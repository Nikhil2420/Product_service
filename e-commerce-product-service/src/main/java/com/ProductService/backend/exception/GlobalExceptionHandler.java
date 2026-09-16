package com.ProductService.backend.exception;

import com.ProductService.backend.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(
            UserNotFoundException userNotFoundException,
            HttpServletRequest request
    ) {
        return buildExceptionObject(userNotFoundException, request, "USER_NOT_FOUND");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handelProductNotFound(
            ProductNotFoundException productNotFoundException,
            HttpServletRequest request
    ) {
        return buildExceptionObject(productNotFoundException, request, "PRODUCT_NOT_FOUND");
    }

    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handelPurchaseNotFound(
            PurchaseNotFoundException purchaseNotFoundException,
            HttpServletRequest request
    ) {
        return buildExceptionObject(purchaseNotFoundException, request, "PURCHASE_NOT_FOUND");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handelCategoryNotFound(
            CategoryNotFoundException categoryNotFoundException,
            HttpServletRequest request
    ) {
        return buildExceptionObject(categoryNotFoundException, request, "CATEGORY_NOT_FOUND");
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handelCartNotFound(
            CartNotFoundException cartNotFoundException,
            HttpServletRequest request
    ) {
        return buildExceptionObject(cartNotFoundException, request, "CART_NOT_FOUND");
    }


    private ResponseEntity<ErrorResponseDto> buildExceptionObject(Exception exception, HttpServletRequest request, String error) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(error)
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponseDto);
    }

}
