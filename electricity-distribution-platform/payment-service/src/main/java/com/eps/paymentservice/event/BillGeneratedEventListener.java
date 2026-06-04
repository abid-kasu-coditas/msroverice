package com.eps.paymentservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillGeneratedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(BillGeneratedEventListener.class);

    @KafkaListener(topics = "bill-generated", groupId = "payment-service-group")
    public void handleBillGenerated(String event) {
        logger.info("Bill generated event received for payment tracking: {}", event);
    }
}
