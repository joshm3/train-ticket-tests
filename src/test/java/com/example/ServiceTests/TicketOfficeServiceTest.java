package com.example.ServiceTests;

import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest
public class TicketOfficeServiceTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:18888/office";

    @Test
    public void runTests() {
        testAddOffice();
        testUpdateOffice();
        testGetAllOffices();
        testDeleteOffice();
        testGetSpecificOffices();
        testGetRegionList();
    }

    public void testGetRegionList() {
        // Testing the /getRegionList endpoint
        String url = BASE_URL + "/getRegionList";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        System.out.println("Response from /getRegionList: " + response.getBody());
        assertTrue(response.getBody().contains("{\"province\":\"Shanghai\",\"cities\":[{\"city\":\"Shanghai\",\"regions\":[{\"region\":\"Pudong New Area\"}"));
    }

    public void testGetAllOffices() {
        // Testing the /getAll endpoint
        String url = BASE_URL + "/getAll";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("{\"name\":\"Jinqiao Road ticket sales outlets\",\"city\":\"Shanghai\",\"province\":\"Shanghai\",\"region\":\"Pudong New Area\",\"address\":\"Jinqiao Road 1320, Shanghai, Pudong New Area\",\"workTime\":\"08:00-18:00\",\"windowNum\":1}"));
        System.out.println("Response from /getAll: " + response.getBody());
    }

    public void testGetSpecificOffices() {
        // Testing the /getSpecificOffices endpoint with POST request
        String url = BASE_URL + "/getSpecificOffices";
        String requestBody = "{\"province\":\"Shanghai\", \"city\":\"Shanghai\", \"region\":\"Pudong New Area\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("{\"name\":\"Jinqiao Road ticket sales outlets\",\"city\":\"Shanghai\",\"province\":\"Shanghai\",\"region\":\"Pudong New Area\",\"address\":\"Jinqiao Road 1320, Shanghai, Pudong New Area\",\"workTime\":\"08:00-18:00\",\"windowNum\":1}"));
        System.out.println("Response from /getSpecificOffices: " + response.getBody());
    }

    public void testAddOffice() {
        // Testing the /addOffice endpoint with POST request
        String url = BASE_URL + "/addOffice";
        String requestBody = "{\"province\":\"newProvince\", \"city\":\"newCity\", \"region\":\"newRegion\", \"office\": {\"name\":\"New Office\", \"address\":\"123 New Address\", \"workTime\":\"09:00-17:00\", \"windowNum\":1}}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("\"insert succeed.\"", response.getBody());
        System.out.println("Response from /addOffice: " + response.getBody());
    }

    public void testUpdateOffice() {
        // Testing the /updateOffice endpoint with POST request
        String url = BASE_URL + "/updateOffice";
        String requestBody = "{\"province\":\"newProvince\", \"city\":\"newCity\", \"region\":\"newRegion\", \"oldOfficeName\":\"New Office\", \"newOffice\": {\"name\":\"Updated Office\", \"address\":\"123 Updated Address\", \"workTime\":\"09:00-18:00\", \"windowNum\":2}}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // assertTrue(response.getBody().contains("\"changedRows\":1"));
        System.out.println("Response from /updateOffice: " + response.getBody());
    }

    public void testDeleteOffice() {
        // Testing the /deleteOffice endpoint with POST request
        String url = BASE_URL + "/deleteOffice";
        String requestBody = "{\"province\":\"newProvince\", \"city\":\"newCity\", \"region\":\"newRegion\", \"officeName\": \"Updated Office\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // assertTrue(response.getBody().contains("\"affectedRows\":1"));
        System.out.println("Response from /deleteOffice: " + response.getBody());
    }


}
