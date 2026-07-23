package com.israf.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IsrafApplication {
	public static void main(String[] args) {
		SpringApplication.run(IsrafApplication.class, args);
	}

}
