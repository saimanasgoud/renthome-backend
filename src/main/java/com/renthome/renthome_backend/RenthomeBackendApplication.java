package com.renthome.renthome_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.renthome.renthome_backend.entity")
@EnableJpaRepositories(basePackages = "com.renthome.renthome_backend.repository")
public class RenthomeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenthomeBackendApplication.class, args);
    }
}
