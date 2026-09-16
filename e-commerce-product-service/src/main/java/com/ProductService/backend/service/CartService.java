package com.ProductService.backend.service;

import com.ProductService.backend.constants.ShippingStatus;
import com.ProductService.backend.dto.*;
import com.ProductService.backend.entity.*;
import com.ProductService.backend.exception.CartNotFoundException;
import com.ProductService.backend.exception.ProductNotFoundException;
import com.ProductService.backend.exception.UserNotFoundException;
import com.ProductService.backend.repository.CartRepository;
import com.ProductService.backend.repository.ProductRepository;
import com.ProductService.backend.repository.PurchaseRepository;
import com.ProductService.backend.repository.UserRepository;
import com.ProductService.backend.utility.ProductMapper;
import com.ProductService.backend.utility.PurchaseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;

    /*
        Transactional ensure if all operation succeed commit
        otherwise rollback
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CartResponseDto addProductToCart(CartRequestDto cartRequestDto) {
        User user = findUserOrElseThrowException(cartRequestDto.getUserId());
        Cart cart = fetchCartFromUser(user);
        List<Product> productList = findProductsOrElseThrowException(cartRequestDto.getProductQuantityDto());
        cart = (cart == null) ? createCartEntity(user, productList, cartRequestDto) : cart;
        Cart finalCart = cart;
        productList.forEach(product -> {
            product.setCart(finalCart);
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
        Cart cart = new Cart();
        int capacity = cart.getCurrentCapacity();
        if (capacity + productCount > Cart.MAX_CAPACITY) {
            throw new RuntimeException("product exceeds limit" + maxCapacity + "space Left" + (maxCapacity - capacity));
        }
        cart.setUser(user);
        cart.setProducts(productList);
        cart.setCurrentCapacity(capacity + productCount);
        cartRepository.save(cart);
        return cart;
    }


    public User findUserOrElseThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for this userId" + userId));
    }

    private List<Product> findProductsOrElseThrowException(List<ProductQuantityDto> productQuantityDto) {
        return productQuantityDto.stream()
                .map(productQuantityDto1 -> {
                    return productRepository.findById(productQuantityDto1.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("No product found for this productId" + productQuantityDto1.getProductId()));
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

    public Cart fetchCartFromUser(User user) {
        return user.getCart();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BuyProductFromCartResponseDto buyProductFromCart(BuyProductFromCartRequestDto buyProductFromCartRequestDto,
                                                            Long userId) {

        User user = findUserOrElseThrowException(userId);
        Cart cart = user.getCart();
        List<Product> products = cart.getProducts();
        List<Long> productIds = buyProductFromCartRequestDto.getProductIds();
        Set<Long> set = new HashSet<>(productIds);
        List<Product> productList = new ArrayList<>();
        List<ProductNameAndPriceDto> productNameAndPriceDto = new ArrayList<>();
        double totalPrice = 0.0;
        Address address = user.getAddress();
        if(address==null){
            throw new RuntimeException("Address for the user is null "+ " "+userId);
        }
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .numberOfDays(5)
                .shippingStatus(ShippingStatus.PICKED)
                .build();
        address.setDeliveryInfo(deliveryInfo);
        for (Product product : products) {
            if(product.getStockQuantity()==0 || !product.isAvailable()){
               continue;
            }
            //logic if product does not belong to cart is should cannot be buyed
            if (set.contains(product.getProductId())) {
                Purchase purchase = Purchase.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .price(product.getProductPrice())
                        .totalAmount(product.getProductPrice())
                        .quantity(1)
                        .paymentMethod(buyProductFromCartRequestDto.getPaymentMethod())
                        .paymentStatus(PurchaseUtility.checkPaymentStatus(buyProductFromCartRequestDto.getPaymentMethod()))
                        .orderDate(LocalDateTime.now())
                        .user(user)
                        .address(address)
                        .build();
                purchaseRepository.save(purchase);
                productList.add(product);
                totalPrice += product.getProductPrice();
                productNameAndPriceDto.add(new ProductNameAndPriceDto(product.getProductName(), product.getProductPrice()));
                product.setStockQuantity(product.getStockQuantity()-1);
                if(product.getStockQuantity()==0){
                    product.setAvailable(false);
                }
                productRepository.save(product);
            }
        }
        if (buyProductFromCartRequestDto.getAmount() < totalPrice) {
            throw new RuntimeException("amount provided is lesser than total price" + ":" + totalPrice);
        }

        //createPurchaseEntity
        productList
                .forEach(product -> {
                    cart.getProducts().remove(product);
                    cartRepository.save(cart);
                });
        DeliveryInfoDto deliveryInfoDto = DeliveryInfoDto.builder()
                .addressDto(PurchaseUtility.mapAddressToAddressDto(user.getAddress()))
                .shippingStatus(ShippingStatus.PICKED)
                .numberOfDays(5)
                .build();
        return BuyProductFromCartResponseDto.builder()
                .productNameAndPriceDtos(productNameAndPriceDto)
                .deliveryInfoDto(deliveryInfoDto)
                .totalPrice(totalPrice)
                .orderDateAndTime(LocalDateTime.now())
                .paymentStatus(PurchaseUtility.checkPaymentStatus(buyProductFromCartRequestDto.getPaymentMethod()))
                .build();


    }

    public List<ProductDto> getAllProductFromCart(Long userId){
        User user=findUserOrElseThrowException(userId);
        Cart cart=user.getCart();
        if(cart==null){
            throw new CartNotFoundException("Cart is empty for the user with userId"+" "+userId);
        }
        List<Product> products=cart.getProducts();
        return ProductMapper.mapProductToProductDto(products);
    }
}
