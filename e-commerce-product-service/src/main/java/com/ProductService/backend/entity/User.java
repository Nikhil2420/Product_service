package com.ProductService.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Table(name = "users_table")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    /*
        as id is sent by the backend user service
        that's why it is not we are not using generated value
     */
    @Id
    private Long userId;

    @NotBlank(message = "userName should be provided")
    private String userName;
    @NotBlank(message = "user email should not be empty or blank")
    @Email(message = "provide valid email format like xyz@gmail.com")
    private String userEmail;
    @NotBlank(message = "userRole should be present")
    private String userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Purchase> purchases;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_fk_id",referencedColumnName = "cartId")
    private Cart cart;

    //logic for these
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_fk_id",referencedColumnName = "addressId")
    private Address address;

}
