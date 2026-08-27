package com.ProductService.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false,unique = true)
    private String productName;

    private long stockQuantity;
    private boolean isAvailable;
    private double productPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    //Create/use a column called category_id in product_table to store the ID of the associated Category.
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Called automatically before INSERT
    // Run this method automatically just before a new entity is INSERTED into the database.
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Called automatically before UPDATE
    //Run this method automatically before an existing entity is updated in the database.
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
