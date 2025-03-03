package com.w2m.starshipregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StarshipRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarshipRegistryApplication.class, args);
	}

}
