package com.ProductService.backend.entity;

import com.ProductService.backend.constants.PaymentMethod;
import com.ProductService.backend.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime orderDate;

    private Long userId;
    private String userName;
    private String role;
    @OneToOne(cascade=CascadeType.ALL)
    /*
        name : What is the name of the foreign-key column in the current entity's table?
        referencedColumnName: Which column in the Address table does that foreign key point to?
     */
    /*
        unique = true should prevent the same Address row from being referenced by two different Purchase rows
     */
    @JoinColumn(name="address_fk_id",referencedColumnName = "addressId",unique = true)
    private Address address;

    @PrePersist
    public void onCreate(){
        orderDate=LocalDateTime.now();
    }
}
