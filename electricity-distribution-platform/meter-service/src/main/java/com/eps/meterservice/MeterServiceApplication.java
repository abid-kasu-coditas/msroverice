package com.eps.meterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eps")
public class MeterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeterServiceApplication.class, args);
    }

}
