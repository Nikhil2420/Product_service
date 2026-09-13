package com.ProductService.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users_cart")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    //static field does not add column in db since this property belongs to class
    public static int MAX_CAPACITY = 20;
    private int currentCapacity;

    @OneToMany(mappedBy = "cart")
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    private User user;

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

}
