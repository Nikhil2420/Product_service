package com.ProductService.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true, length = 100)
    private String categoryName;

    /*
        mappedBy makes the relationship bidirectional without making the
        inverse side the owner(that is fk is managed only one side)
        it tells Hibernate that the foreign key
        is managed by the other entity's field.
    */
    @OneToMany(mappedBy = "category")
    private List<Product> productList = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addProduct(Product product) {
        productList.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product) {
        productList.remove(product);
        product.setCategory(null);
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
