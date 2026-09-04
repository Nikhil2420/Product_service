package com.ProductService.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "address_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String city;

    private String state;

    private String pinCode;

    private String street;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_info_fk", referencedColumnName = "deliveryInfoId")
    private DeliveryInfo deliveryInfo;
}
