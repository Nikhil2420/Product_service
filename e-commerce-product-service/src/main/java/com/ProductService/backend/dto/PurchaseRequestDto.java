package com.ProductService.backend.dto;

import com.ProductService.backend.constants.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseRequestDto {

    @NotNull
    private Long productId;

    @NotBlank
    private String productName;
    @NotNull
    @Valid
    private AddressDto addressDto;
    @Min(1)
    private double amount;
    @Min(1)
    private int quantity;

    private PaymentMethod paymentMethod;
}
