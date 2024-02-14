package com.shopwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ShopwaFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopwaFrontEndApplication.class, args);
	}

}
