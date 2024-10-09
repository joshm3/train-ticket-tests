package com.example.ServiceTests;

import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.nio.file.Files;
import java.nio.file.Paths;


@SpringBootTest
public class AvatarServiceTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:18888/api/v1/avatar";

    @Test
    public void testAvatar() {
        String imageName = "HumanFace.jpg";

        String base64Image = null;
        try {
            ClassPathResource resource = new ClassPathResource(imageName);
            byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
            base64Image = Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            System.err.println("EXCEPTION IN AVATARSERVICETEST " + e.getClass() + " " +  e.getMessage());
        }
        assertNotNull(base64Image);

        // Prepare the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("img", base64Image);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);

        // Asserting the response
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        String base64response = response.getBody();
        byte[] responseContent = Base64.getDecoder().decode(base64response);
        try {
            Files.write(Paths.get("ResponseImage.jpg"), responseContent);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        }

        System.out.println("\n################################################################");
        System.out.println("Response from server is located at ./ResponseImage.jpg");
        System.out.println("Verify the image is a valid jpg and is zoomed in around the face");
        System.out.println("################################################################\n");

        System.out.println(response.getBody());
    }
}
