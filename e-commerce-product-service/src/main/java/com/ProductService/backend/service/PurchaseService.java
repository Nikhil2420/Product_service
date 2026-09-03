package com.ProductService.backend.service;

import com.ProductService.backend.constants.PaymentMethod;
import com.ProductService.backend.constants.PaymentStatus;
import com.ProductService.backend.constants.ShippingStatus;
import com.ProductService.backend.dto.*;
import com.ProductService.backend.entity.Address;
import com.ProductService.backend.entity.Product;
import com.ProductService.backend.entity.Purchase;
import com.ProductService.backend.repository.ProductRepository;
import com.ProductService.backend.repository.PurchaseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseResponseDto buyProduct(@Valid PurchaseRequestDto purchaseRequestDto) {

        ProductResponseDto productResponseDto = productService.getProduct(purchaseRequestDto.getProductId());

        validateInputRequest(purchaseRequestDto, productResponseDto);


        Product product = productRepository.findById(purchaseRequestDto.getProductId()).orElseThrow(() ->
                new RuntimeException("No product found for this productId" + ":" + purchaseRequestDto.getProductId()));
        product.setStockQuantity(product.getStockQuantity() - purchaseRequestDto.getQuantity());
        productRepository.save(product);
        checkUserDetails(purchaseRequestDto);
        createPurchaseEntity(purchaseRequestDto, productResponseDto);
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .addressDto(purchaseRequestDto.getAddressDto())
                .numberOfDays(calculateDaysBasedOnLocation(purchaseRequestDto.getAddressDto()))
                .shippingStatus(ShippingStatus.PICKED)
                .build();
        return PurchaseResponseDto.builder()
                .productName(purchaseRequestDto.getProductName())
                .price(productResponseDto.getProductPrice())
                .orderDate(LocalDate.now())
                .paymentStatus(checkPaymentStatus(purchaseRequestDto.getPaymentMethod()))
                .deliveryInfo(deliveryInfo)
                .userId(purchaseRequestDto.getUserId())
                .userName(purchaseRequestDto.getUserName())
                .role(purchaseRequestDto.getRole())
                .build();

    }

    private void checkUserDetails(PurchaseRequestDto purchaseRequestDto) {
        if(purchaseRequestDto.getUserName() == null){
            purchaseRequestDto.setUserName("Testing");
        }
        if(purchaseRequestDto.getUserId()==null){
            purchaseRequestDto.setUserId(0L);
        }

        if(purchaseRequestDto.getRole()==null){
            purchaseRequestDto.setRole("Testing");
        }
    }

    private void createPurchaseEntity(PurchaseRequestDto purchaseRequestDto, ProductResponseDto productResponseDto) {
        Address address = Address.builder()
                .state(purchaseRequestDto.getAddressDto().getState())
                .city(purchaseRequestDto.getAddressDto().getCity())
                .street(purchaseRequestDto.getAddressDto().getStreet())
                .pinCode(purchaseRequestDto.getAddressDto().getPinCode())
                .build();
//        checkDuplicateAddress(address);

        Purchase purchase = Purchase.builder()
                .productId(purchaseRequestDto.getProductId())
                .productName(purchaseRequestDto.getProductName())
                .price(productResponseDto.getProductPrice())
                .totalAmount(purchaseRequestDto.getAmount())
                .quantity(purchaseRequestDto.getQuantity())
                .paymentMethod(purchaseRequestDto.getPaymentMethod())
                .paymentStatus(checkPaymentStatus(purchaseRequestDto.getPaymentMethod()))
                .address(address)
                .userId(purchaseRequestDto.getUserId())
                .userName(purchaseRequestDto.getUserName())
                .role(purchaseRequestDto.getRole())
                .build();
        purchaseRepository.save(purchase);

    }

    public int calculateDaysBasedOnLocation(AddressDto addressDto) {
        //logic to calculate no. of days to deliver the order
        //for now we are returning 5 for testing
        return 5;
    }

    public PaymentStatus checkPaymentStatus(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CASH || paymentMethod == PaymentMethod.CASH_ON_DELIVERY) {
            return PaymentStatus.PENDING;
        }
        return PaymentStatus.PAID;
    }

    public void validateInputRequest(PurchaseRequestDto purchaseRequestDto, ProductResponseDto productResponseDto) {
        if (purchaseRequestDto.getAmount() < productResponseDto.getProductPrice() * purchaseRequestDto.getQuantity()) {
            throw new RuntimeException("Unsufficient Amount for the product" + productResponseDto.getProductPrice());
        } else if (purchaseRequestDto.getQuantity() > productResponseDto.getStockQuantity()) {
            throw new RuntimeException("only this much stock is present for the product" + ":" + productResponseDto.getStockQuantity());
        }
    }

}
