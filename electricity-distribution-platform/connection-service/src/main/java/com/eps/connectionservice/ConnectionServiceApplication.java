package com.eps.connectionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eps")
public class ConnectionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectionServiceApplication.class, args);
    }

}
