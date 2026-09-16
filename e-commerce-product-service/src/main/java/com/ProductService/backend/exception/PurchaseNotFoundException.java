package com.ProductService.backend.exception;

public class PurchaseNotFoundException extends RuntimeException{

    public PurchaseNotFoundException(String message){
        super(message);
    }
}
