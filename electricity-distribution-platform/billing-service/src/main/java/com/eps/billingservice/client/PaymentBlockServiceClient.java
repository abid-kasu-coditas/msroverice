package com.eps.billingservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Client for communicating with Payment Service to check if a customer is blocked.
 * Used by Billing Service to prevent billing blocked customers.
 */
@Component
public class PaymentBlockServiceClient {

    @Value("${payment-service.url:http://localhost:8086}")
    private String paymentServiceUrl;

    private final RestTemplate restTemplate;

    public PaymentBlockServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Check if a customer is blocked due to non-payment
     *
     * @param customerId UUID of the customer
     * @return true if customer is blocked, false otherwise
     */
    public boolean isCustomerBlocked(Long customerId) {
        try {
            String url = paymentServiceUrl + "/api/payments/blocks/status/{customerId}";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, customerId);
            if (response != null && response.has("isBlocked")) {
                return response.get("isBlocked").asBoolean();
            }
            return false;
        } catch (RestClientException e) {
            System.err.println("Error calling Payment Service: " + e.getMessage());
            // Default to not blocked if service is unavailable
            return false;
        }
    }

    /**
     * Get blocking details for a customer
     *
     * @param customerId UUID of the customer
     * @return JsonNode containing blocking details or null if not blocked
     */
    public JsonNode getBlockingDetails(Long customerId) {
        try {
            String url = paymentServiceUrl + "/api/payments/blocks/customer/{customerId}";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, customerId);
            return response;
        } catch (RestClientException e) {
            System.err.println("Error calling Payment Service: " + e.getMessage());
            return null;
        }
    }
}
