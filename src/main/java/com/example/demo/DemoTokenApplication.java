package com.example.demo;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoTokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoTokenApplication.class, args);
	}

	@Bean
	RequestDumperFilter requestDumperFilter() {
		return new RequestDumperFilter();
	}
}
