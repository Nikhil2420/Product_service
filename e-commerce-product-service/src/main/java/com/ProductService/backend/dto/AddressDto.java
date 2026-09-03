package com.ProductService.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    @NotEmpty
    private String city;
    @NotEmpty
    private String state;
    @NotEmpty
    private String pinCode;
    @NotEmpty
    private String street;
}
