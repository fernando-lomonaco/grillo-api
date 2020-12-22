package br.com.grillo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class GrilloApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrilloApplication.class, args);
	}

}
