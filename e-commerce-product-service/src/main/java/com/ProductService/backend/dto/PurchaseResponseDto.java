package com.ProductService.backend.dto;

import com.ProductService.backend.constants.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseResponseDto {

    private String productName;
    private double price;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDateTime;
    private DeliveryInfoDto deliveryInfoDto;
    private Long userId;
    private String userName;
    private String role;
}
