package com.globits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.globits.da.repository")
public class DAApplication {
	public static void main(String[] args) {
		SpringApplication.run(DAApplication.class, args);
	}
}
