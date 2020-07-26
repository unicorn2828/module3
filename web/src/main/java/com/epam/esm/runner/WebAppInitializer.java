package com.epam.esm.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * This is the WebAppInitializer class; it contains the main method.
 * This is an entry point to the program.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@EntityScan("com.epam.esm.*")
@EnableJpaRepositories("com.epam.esm.*")
@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class WebAppInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebAppInitializer.class, args);
    }
}
