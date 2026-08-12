package com.sorts.srms.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SrmsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrmsBackendApplication.class, args);
    }
}
