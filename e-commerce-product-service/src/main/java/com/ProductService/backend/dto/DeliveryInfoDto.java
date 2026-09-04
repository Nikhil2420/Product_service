package com.ProductService.backend.dto;

import com.ProductService.backend.constants.ShippingStatus;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryInfoDto {

    private Integer numberOfDays;
    private AddressDto addressDto;
    private ShippingStatus shippingStatus;
}
