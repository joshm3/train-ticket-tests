package com.example.ServiceTests;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
public class VoucherServiceTest {

    private final RestTemplate restTemplate = new RestTemplate();

    //For original
    private static final String BASE_URL = "http://localhost:18888/getVoucher";

    //gets the first orderId from orderservice to test voucher service with
    public String getOrderId() {
        String url = "http://localhost:18888/api/v1/orderservice/order";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String json = response.getBody();
        String regex = "\"id\":\"([a-f0-9\\-]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String firstId = matcher.group(1); // Extracts the first captured group (UUID)
            System.out.println("First ID: " + firstId);
            return firstId;
        } else return "FAILURE";
    }

    @Test
    public void testGetVoucher() {
        // This test can validate if querying for an order returns expected results
        String requestBody = "{\"orderId\":\"" + getOrderId() + "\", \"type\":1}"; // Change as needed
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Assuming the service would return the order details successfully

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, entity, String.class);

        // Asserting the response
        System.out.println(response);
        System.out.println("Response from queryOrderByIdAndType: " + response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("voucher_id")); // Replace with a real key you expect

    }

}