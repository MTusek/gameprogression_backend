package com.example.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GameProgressApplication {
	public static void main(String[] args) {
		SpringApplication.run(GameProgressApplication.class, args);
	}

	@RequestMapping("/")
	public String helloWorld(){
		return "Hello there!";
	}
}