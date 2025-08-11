package com.java.healSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.java.healSync")
public class HealSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealSyncApplication.class, args);
	}
}
