package com.ProductService.backend.repository;

import com.ProductService.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByIsAvailableTrue(Pageable pageable);
}
