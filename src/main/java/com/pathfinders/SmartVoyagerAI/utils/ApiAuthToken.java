package com.pathfinders.SmartVoyagerAI.utils;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class ApiAuthToken {

    public static String getAccessToken(String grantType, String clientId, String clientSecret) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "client_credentials");
//        body.add("client_id", "QZ0EAxjWynBHrR7Zm2M5SopXXSibzX7N");
//        body.add("client_secret", "IDnE2wgDlSGKAKzA");
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                "https://test.api.amadeus.com/v1/security/oauth2/token",
                HttpMethod.POST,
                request,
                AuthResponse.class);

        return Objects.requireNonNull(response.getBody()).access_token;
    }
}
