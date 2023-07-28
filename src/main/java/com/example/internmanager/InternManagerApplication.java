package com.example.internmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.internmanager.model")
@EnableJpaRepositories(basePackages = "com.example.internmanager.repository")
public class InternManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternManagerApplication.class, args);
	}

}
