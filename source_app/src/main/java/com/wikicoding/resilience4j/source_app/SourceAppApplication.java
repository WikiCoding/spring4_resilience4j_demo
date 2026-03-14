package com.wikicoding.resilience4j.source_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
//@EnableResilientMethods // necessary for springboot 4 native Retryable
public class SourceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceAppApplication.class, args);
    }

}
