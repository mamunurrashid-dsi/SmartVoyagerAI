package com.pathfinders.SmartVoyagerAI.services;

import com.pathfinders.SmartVoyagerAI.clients.HotelClient;
import com.pathfinders.SmartVoyagerAI.utils.AuthResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class HotelAgent {

    private final ChatClient chatClient;

    private final HotelClient hotelClient;

    public HotelAgent(HotelClient hotelClient, ChatModel chatModel) {
        this.hotelClient = hotelClient;
        this.chatClient = ChatClient.builder(chatModel).build();
    }

//    @Tool(name = "get-hotel", description = "Gets hotel by a city name")
//    public String getHotelData(String destination) {
//        return hotelClient.getHotelInfo(destination);
//    }

    @Tool(name = "get-hotel", description = "Get hotel by cityCode.")
    public String getHotelsInCity(String cityCode) {

        String token = getAmadeusToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> apiResponse = restTemplate.exchange(
                "https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city?cityCode=" + cityCode,
                HttpMethod.GET,
                request,
                String.class);

        String responseBody = apiResponse.getBody();

        assert responseBody != null;
        Prompt prompt = new Prompt(
                new SystemMessage("From the UserMessage, suggest me five suitable hotels"),
                new UserMessage(responseBody)
        );
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        return response;
    }

    public String getAmadeusToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", "QZ0EAxjWynBHrR7Zm2M5SopXXSibzX7N");
        body.add("client_secret", "IDnE2wgDlSGKAKzA");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                "https://test.api.amadeus.com/v1/security/oauth2/token",
                HttpMethod.POST,
                request,
                AuthResponse.class);

        return response.getBody().access_token;
    }
}
