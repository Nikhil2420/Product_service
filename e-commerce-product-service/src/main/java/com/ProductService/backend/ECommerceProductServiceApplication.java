package com.ProductService.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ECommerceProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceProductServiceApplication.class, args);
	}

}
