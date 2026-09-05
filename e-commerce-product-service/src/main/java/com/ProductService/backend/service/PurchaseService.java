package com.ProductService.backend.service;

import com.ProductService.backend.constants.ShippingStatus;
import com.ProductService.backend.dto.DeliveryInfoDto;
import com.ProductService.backend.dto.ProductResponseDto;
import com.ProductService.backend.dto.PurchaseRequestDto;
import com.ProductService.backend.dto.PurchaseResponseDto;
import com.ProductService.backend.entity.Address;
import com.ProductService.backend.entity.DeliveryInfo;
import com.ProductService.backend.entity.Product;
import com.ProductService.backend.entity.Purchase;
import com.ProductService.backend.repository.ProductRepository;
import com.ProductService.backend.repository.PurchaseRepository;
import com.ProductService.backend.utility.PurchaseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseResponseDto buyProduct(@Valid PurchaseRequestDto purchaseRequestDto) {

        ProductResponseDto productResponseDto = productService.getProduct(purchaseRequestDto.getProductId());

        PurchaseUtility.validateInputRequest(purchaseRequestDto, productResponseDto);


        Product product = productRepository.findById(purchaseRequestDto.getProductId()).orElseThrow(() ->
                new RuntimeException("No product found for this productId" + ":" + purchaseRequestDto.getProductId()));
        product.setStockQuantity(product.getStockQuantity() - purchaseRequestDto.getQuantity());
        productRepository.save(product);
        PurchaseUtility.checkUserDetails(purchaseRequestDto);

        DeliveryInfoDto deliveryInfoDto = DeliveryInfoDto.builder()
                .addressDto(purchaseRequestDto.getAddressDto())
                .numberOfDays(PurchaseUtility.calculateDaysBasedOnLocation(purchaseRequestDto.getAddressDto()))
                .shippingStatus(ShippingStatus.PICKED)
                .build();
        createPurchaseEntity(purchaseRequestDto, productResponseDto, deliveryInfoDto);
        return PurchaseResponseDto.builder()
                .productName(purchaseRequestDto.getProductName())
                .price(productResponseDto.getProductPrice())
                .orderDateTime(LocalDateTime.now())
                .paymentStatus(PurchaseUtility.checkPaymentStatus(purchaseRequestDto.getPaymentMethod()))
                .deliveryInfoDto(deliveryInfoDto)
                .userId(purchaseRequestDto.getUserId())
                .userName(purchaseRequestDto.getUserName())
                .role(purchaseRequestDto.getRole())
                .build();

    }


    public void createPurchaseEntity(PurchaseRequestDto purchaseRequestDto, ProductResponseDto productResponseDto, DeliveryInfoDto deliveryInfoDto) {
        Address address = Address.builder()
                .state(purchaseRequestDto.getAddressDto().getState())
                .city(purchaseRequestDto.getAddressDto().getCity())
                .street(purchaseRequestDto.getAddressDto().getStreet())
                .pinCode(purchaseRequestDto.getAddressDto().getPinCode())
                .deliveryInfo(new DeliveryInfo(deliveryInfoDto.getNumberOfDays(), deliveryInfoDto.getShippingStatus()))
                .build();
//        checkDuplicateAddress(address);

        Purchase purchase = Purchase.builder()
                .productId(purchaseRequestDto.getProductId())
                .productName(purchaseRequestDto.getProductName())
                .price(productResponseDto.getProductPrice())
                .totalAmount(purchaseRequestDto.getAmount())
                .quantity(purchaseRequestDto.getQuantity())
                .paymentMethod(purchaseRequestDto.getPaymentMethod())
                .paymentStatus(PurchaseUtility.checkPaymentStatus(purchaseRequestDto.getPaymentMethod()))
                .address(address)
                .userId(purchaseRequestDto.getUserId())
                .userName(purchaseRequestDto.getUserName())
                .role(purchaseRequestDto.getRole())
                .build();
        purchaseRepository.save(purchase);

    }


    public PurchaseResponseDto getPurchaseById(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new RuntimeException("No Purchase found for this purchaseId:" + " " + purchaseId)
        );
        return PurchaseUtility.mapPurchaseToPurchaseResponseDto(purchase);
    }


    public List<PurchaseResponseDto> getPurchaseHistory(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByUserId(userId);
        return PurchaseUtility.mapListOfPurchaseToListOfPurchaseResponseDto(purchases);
    }

}
