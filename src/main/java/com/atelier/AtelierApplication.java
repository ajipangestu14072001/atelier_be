package com.atelier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AtelierApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtelierApplication.class, args);
	}

}
