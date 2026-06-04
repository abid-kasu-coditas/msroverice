package com.eps.paymentservice.util;

import com.eps.paymentservice.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayMock {

  public PaymentStatus processPayment(String transactionNumber, Double amount,
      String paymentMethod) {
    boolean success = validatePayment(transactionNumber, amount);

    if (success) {
      return PaymentStatus.COMPLETED;
    } else {
      return PaymentStatus.FAILED;
    }
  }

  private boolean validatePayment(String transactionNumber, Double amount) {
    if (transactionNumber == null || transactionNumber.isEmpty()) {
      return false;
    }

    if (amount == null || amount <= 0) {
      return false;
    }

    int randomFactor = (int) (System.currentTimeMillis() % 100);
    return randomFactor < 95;
  }

  public String generateReferenceNumber() {
    return "REF_" + System.currentTimeMillis();
  }
}
