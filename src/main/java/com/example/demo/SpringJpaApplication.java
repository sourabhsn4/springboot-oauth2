package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.web.model"})
@EnableJpaRepositories(basePackages = "com.example.web.repository")
//@ComponentScan(basePackages = {"com.example.web.controllers", "com.example.web.service"})
@ComponentScan(basePackages = {"com.example.web.*"})
public class SpringJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);		
	}

}
