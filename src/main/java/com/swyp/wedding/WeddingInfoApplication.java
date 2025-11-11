package com.swyp.wedding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WeddingInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeddingInfoApplication.class, args);
	}

}
