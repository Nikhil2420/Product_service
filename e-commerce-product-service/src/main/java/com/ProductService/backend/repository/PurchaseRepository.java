package com.ProductService.backend.repository;

import com.ProductService.backend.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /*
        why findByUserId_UserId?
        Passing userId is fine; the problem is findByUserId() searches for userId
        directly in Purchase, while your path is Purchase.user → User.userId,
        so use findByUser_UserId(userId).
     */

    /*
            Purchase has userId directly
                ↓
            findByUserId()

            Purchase has User user
            User has userId
                ↓
            findByUser_UserId()
     */
    List<Purchase> findByUser_UserId(Long userId);
}
