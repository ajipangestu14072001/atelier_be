package com.atelier;

import com.atelier.common.config.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@SpringBootApplication()
public class AtelierApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtelierApplication.class, args);
	}

}
