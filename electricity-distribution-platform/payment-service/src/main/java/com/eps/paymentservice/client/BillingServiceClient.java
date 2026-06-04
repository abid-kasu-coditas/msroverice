package com.eps.paymentservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpMethod;

/**
 * Client for communicating with Billing Service to update bill status after payment.
 * Used by Payment Service to synchronize payment status with bill records.
 */
@Component
public class BillingServiceClient {

    @Value("${billing-service.url:http://localhost:8085}")
    private String billingServiceUrl;

    private final RestTemplate restTemplate;

    public BillingServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Get bill details by bill ID
     *
     * @param billId UUID of the bill
     * @return JsonNode containing bill details
     */
    public JsonNode getBill(Long billId) {
        try {
            String url = billingServiceUrl + "/api/bills/{id}";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, billId);
            return response;
        } catch (RestClientException e) {
            System.err.println("Error calling Billing Service: " + e.getMessage());
            throw new RuntimeException("Failed to get bill from Billing Service", e);
        }
    }

    /**
     * Update bill status to PAID after successful payment
     *
     * @param billId UUID of the bill
     * @param amount Amount paid
     * @return JsonNode containing updated bill details
     */
    public JsonNode updateBillStatusToPaid(Long billId, Double amount) {
        try {
            String url = billingServiceUrl + "/api/bills/{id}/payment-status?amount={amount}";
            JsonNode response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                null,
                JsonNode.class,
                billId,
                amount
            ).getBody();
            return response;
        } catch (RestClientException e) {
            System.err.println("Error calling Billing Service: " + e.getMessage());
            throw new RuntimeException("Failed to update bill status in Billing Service", e);
        }
    }

    /**
     * Get bills for a customer
     *
     * @param customerId UUID of the customer
     * @return JsonNode containing list of bills
     */
    public JsonNode getCustomerBills(Long customerId) {
        try {
            String url = billingServiceUrl + "/api/bills/customer/{customerId}";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, customerId);
            return response;
        } catch (RestClientException e) {
            System.err.println("Error calling Billing Service: " + e.getMessage());
            return null;
        }
    }

}
