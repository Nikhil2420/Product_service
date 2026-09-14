package com.ProductService.backend.dto;

import com.ProductService.backend.constants.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyProductFromCartRequestDto {

    @NotEmpty(message = "product id should not be empty")
    private List<@NotNull(message = "product id should not be null") Long> productIds;
    private PaymentMethod paymentMethod;
    private double amount;
}
