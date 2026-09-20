package com.ProductService.backend.repository;

import com.ProductService.backend.entity.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo,Long> {
}
