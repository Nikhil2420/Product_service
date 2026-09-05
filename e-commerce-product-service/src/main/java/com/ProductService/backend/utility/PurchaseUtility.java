package com.ProductService.backend.utility;

import com.ProductService.backend.constants.PaymentMethod;
import com.ProductService.backend.constants.PaymentStatus;
import com.ProductService.backend.dto.*;
import com.ProductService.backend.entity.Address;
import com.ProductService.backend.entity.Purchase;

import java.util.List;

public class PurchaseUtility {

    public static void checkUserDetails(PurchaseRequestDto purchaseRequestDto) {
        if (purchaseRequestDto.getUserName() == null) {
            purchaseRequestDto.setUserName("Testing");
        }
        if (purchaseRequestDto.getUserId() == null) {
            purchaseRequestDto.setUserId(0L);
        }

        if (purchaseRequestDto.getRole() == null) {
            purchaseRequestDto.setRole("Testing");
        }
    }

    public static int calculateDaysBasedOnLocation(AddressDto addressDto) {
        //logic to calculate no. of days to deliver the order
        //for now we are returning 5 for testing
        return 5;
    }

    public static PaymentStatus checkPaymentStatus(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CASH || paymentMethod == PaymentMethod.CASH_ON_DELIVERY) {
            return PaymentStatus.PENDING;
        }
        return PaymentStatus.PAID;
    }

    public static void validateInputRequest(PurchaseRequestDto purchaseRequestDto, ProductResponseDto productResponseDto) {
        if (purchaseRequestDto.getAmount() < productResponseDto.getProductPrice() * purchaseRequestDto.getQuantity()) {
            throw new RuntimeException("Unsufficient Amount for the product" + productResponseDto.getProductPrice());
        } else if (purchaseRequestDto.getQuantity() > productResponseDto.getStockQuantity()) {
            throw new RuntimeException("only this much stock is present for the product" + ":" + productResponseDto.getStockQuantity());
        }
    }

    public static PurchaseResponseDto mapPurchaseToPurchaseResponseDto(Purchase purchase) {
        return PurchaseResponseDto.builder()
                .userId(purchase.getUserId())
                .userName(purchase.getUserName())
                .role(purchase.getRole())
                .price(purchase.getPrice())
                .productName(purchase.getProductName())
                .orderDateTime(purchase.getOrderDate())
                .paymentStatus(purchase.getPaymentStatus())
                .deliveryInfoDto(createDeliveryInfoDto(purchase))
                .build();
    }

    public static DeliveryInfoDto createDeliveryInfoDto(Purchase purchase) {
        Address address = purchase.getAddress();
        return DeliveryInfoDto.builder()
                .shippingStatus(address.getDeliveryInfo().getShippingStatus())
                .numberOfDays(address.getDeliveryInfo().getNumberOfDays())
                .addressDto(mapAddressToAddressDto(purchase.getAddress()))
                .build();

    }

    public static AddressDto mapAddressToAddressDto(Address address) {
        return AddressDto.builder()
                .state(address.getState())
                .street(address.getStreet())
                .city(address.getCity())
                .pinCode(address.getPinCode())
                .build();
    }

    public static List<PurchaseResponseDto> mapListOfPurchaseToListOfPurchaseResponseDto(List<Purchase> purchases) {
        return purchases.stream()
                .map(PurchaseUtility::mapPurchaseToPurchaseResponseDto).toList();
    }
}
