package com.ProductService.backend.service;

import com.ProductService.backend.dto.*;
import com.ProductService.backend.entity.Cart;
import com.ProductService.backend.entity.Product;
import com.ProductService.backend.entity.User;
import com.ProductService.backend.repository.CartRepository;
import com.ProductService.backend.repository.ProductRepository;
import com.ProductService.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /*
        Transactional ensure if all operation succeed commit
        otherwise rollback
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CartResponseDto addProductToCart(CartRequestDto cartRequestDto) {
        User user = findUserOrElseThrowException(cartRequestDto.getUserId());
        List<Product> productList = findProductsOrElseThrowException(cartRequestDto.getProductQuantityDto());
        Cart cart = createCartEntity(user, productList, cartRequestDto);
        productList.forEach(product -> {
            product.setCart(cart);
            productRepository.save(product);
        });
        user.setCart(cart);
        userRepository.save(user);

        CartResponseDto cartResponseDto = createCartResponseDto(cart, productList, cartRequestDto.getProductQuantityDto());
        cartRepository.save(cart);
        return cartResponseDto;
    }

    private Cart createCartEntity(User user, List<Product> productList, CartRequestDto cartRequestDto) {
        int productCount = numberOfProductInCart(cartRequestDto.getProductQuantityDto());
        int maxCapacity = Cart.MAX_CAPACITY;
        //more logic
        //logic for currentCapacity,maxcapacity
        //existing cart
        Cart cart=new Cart();
        int capacity=cart.getCurrentCapacity();
        if (capacity+productCount>Cart.MAX_CAPACITY) {
            throw new RuntimeException("product exceeds limit" + maxCapacity+"space Left"+(maxCapacity-capacity));
        }
        cart.setUser(user);
        cart.setProducts(productList);
        cart.setCurrentCapacity(capacity+productCount);
        cartRepository.save(cart);
        return cart;
    }


    public User findUserOrElseThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for this userId" + userId));
    }

    private List<Product> findProductsOrElseThrowException(List<ProductQuantityDto> productQuantityDto) {
        return productQuantityDto.stream()
                .map(productQuantityDto1 -> {
                    return productRepository.findById(productQuantityDto1.getProductId())
                            .orElseThrow(() -> new RuntimeException("No product found for this productId" + productQuantityDto1.getProductId()));
                }).toList();
    }

    private int numberOfProductInCart(List<ProductQuantityDto> productQuantityDtos) {
        int count = 0;
        for (ProductQuantityDto productQuantityDto : productQuantityDtos) {
            count += productQuantityDto.getProductQuantity();
        }
        return count;
    }

    private CartResponseDto createCartResponseDto(Cart cart, List<Product> products, List<ProductQuantityDto> productQuantityDtos
    ) {

        Map<Long, ProductNameAndPriceDto> productIdToName = new HashMap<>();
        double totalCartPrice = 0.0;
        int capacityLeftInCart = Cart.MAX_CAPACITY - cart.getCurrentCapacity();
        for (Product product : products) {
            totalCartPrice += product.getProductPrice();
            productIdToName.put(product.getProductId(), new ProductNameAndPriceDto(product.getProductName(), product.getProductPrice()));
        }
        List<CartProductDetail> cartProductDetailList = productQuantityDtos.stream()
                .map(productQuantityDto -> {
                    return CartProductDetail.builder()
                            .productQuantity(productQuantityDto.getProductQuantity())
                            .productName(productIdToName.get(productQuantityDto.getProductId()).ProductName)
                            .productPrice(productIdToName.get(productQuantityDto.getProductId()).productPrice)
                            .build();

                }).toList();

        return CartResponseDto.builder()
                .cartProductDetailList(cartProductDetailList)
                .totalCartPrice(totalCartPrice)
                .capacityLeftInCart(capacityLeftInCart)
                .build();

    }

}
