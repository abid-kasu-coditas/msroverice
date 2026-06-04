package com.eps.customerservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentBlockServiceClient {

    @Value("${payment-service.url:http://localhost:8086}")
    private String paymentServiceUrl;

    private final RestTemplate restTemplate;

    public PaymentBlockServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isCustomerBlocked(UUID customerId) {
        try {
            String url = paymentServiceUrl + "/api/payments/blocks/status/{customerId}";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, customerId);
            return response != null && response.has("isBlocked") && response.get("isBlocked").asBoolean();
        } catch (RestClientException ex) {
            return false;
        }
    }
}
