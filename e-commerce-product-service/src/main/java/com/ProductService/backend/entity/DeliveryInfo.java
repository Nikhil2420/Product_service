package com.ProductService.backend.entity;

import com.ProductService.backend.constants.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryInfoId;

    private Integer numberOfDays;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;


    public DeliveryInfo(Integer numberOfDays,ShippingStatus shippingStatus){
        this.numberOfDays=numberOfDays;
        this.shippingStatus=shippingStatus;
    }
}
