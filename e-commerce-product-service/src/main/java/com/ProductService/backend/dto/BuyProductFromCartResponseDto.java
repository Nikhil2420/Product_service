package com.ProductService.backend.dto;

import com.ProductService.backend.constants.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyProductFromCartResponseDto {

    private List<ProductNameAndPriceDto> productNameAndPriceDtos;
    private double totalPrice;
    private DeliveryInfoDto deliveryInfoDto;
    private LocalDateTime orderDateAndTime;
    private PaymentStatus paymentStatus;
}
