package com.example.jpa_formacion;

import com.example.jpa_formacion.config.ConfiguationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(ConfiguationProperties.class)

public class JpaFormacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaFormacionApplication.class, args);
	}

}
