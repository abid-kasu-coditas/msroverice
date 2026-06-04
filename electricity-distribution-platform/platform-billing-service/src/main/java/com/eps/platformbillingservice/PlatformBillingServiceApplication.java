package com.eps.platformbillingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlatformBillingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlatformBillingServiceApplication.class, args);
  }
}
