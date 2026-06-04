package com.eps.meterreadingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eps")
public class MeterReadingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MeterReadingServiceApplication.class, args);
  }
}
