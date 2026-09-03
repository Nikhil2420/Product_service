package com.ProductService.backend.dto;

import com.ProductService.backend.constants.PaymentStatus;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseResponseDto {

    private String productName;
    private double price;
    private PaymentStatus paymentStatus;
    private LocalDate orderDate;
    private DeliveryInfo deliveryInfo;
}
