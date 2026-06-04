package com.eps.billingservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Client for communicating with Meter Service to retrieve meter readings.
 * Used by Billing Service to get actual consumption data for bill generation.
 */
@Component
public class MeterServiceClient {

    @Value("${meter-service.url:http://localhost:8084}")
    private String meterServiceUrl;

    private final RestTemplate restTemplate;

    public MeterServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Get meter reading by meter ID
     * Retrieves the current and previous readings for a meter account.
     *
     * @param meterId UUID of the meter
     * @return JsonNode containing meter reading details
     * @throws RestClientException if the request fails
     */
    public JsonNode getMeterReadings(Long meterId) {
        try {
            String url = meterServiceUrl + "/api/meters/{id}/readings/latest";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, meterId);
            return response;
        } catch (RestClientException e) {
            System.err.println("Error calling Meter Service: " + e.getMessage());
            throw new RuntimeException("Failed to get meter readings from Meter Service", e);
        }
    }

    /**
     * Get meter account details by meter ID
     *
     * @param meterId UUID of the meter
     * @return JsonNode containing meter account details
     * @throws RestClientException if the request fails
     */
    public JsonNode getMeterAccount(Long meterId) {
        try {
            String url = meterServiceUrl + "/api/meters/{id}";
            JsonNode response = restTemplate.getForObject(url, JsonNode.class, meterId);
            return response;
        } catch (RestClientException e) {
            System.err.println("Error calling Meter Service: " + e.getMessage());
            throw new RuntimeException("Failed to get meter account from Meter Service", e);
        }
    }

    /**
     * Calculate units consumed based on current and previous readings
     *
     * @param currentReading Current meter reading
     * @param previousReading Previous meter reading
     * @return Units consumed
     */
    public static int calculateUnitsConsumed(int currentReading, int previousReading) {
        return Math.max(0, currentReading - previousReading);
    }
}
