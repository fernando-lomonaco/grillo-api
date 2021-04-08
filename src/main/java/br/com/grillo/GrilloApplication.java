package br.com.grillo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class GrilloApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrilloApplication.class, args);
		log.info("GrilloApplication started successfully at {}", LocalDateTime.now());
	}

}
