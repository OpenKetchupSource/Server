package com.openketchupsource.soulmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.openketchupsource.soulmate")
@EnableJpaAuditing
public class SoulmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoulmateApplication.class, args);
	}

}
