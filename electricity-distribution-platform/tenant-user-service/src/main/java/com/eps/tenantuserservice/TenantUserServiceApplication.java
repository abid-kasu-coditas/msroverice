package com.eps.tenantuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eps")
public class TenantUserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TenantUserServiceApplication.class, args);
  }
}
