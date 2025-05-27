package com.pathfinders.SmartVoyagerAI.services;

import com.pathfinders.SmartVoyagerAI.utils.ApiAuthToken;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HotelAgent {

    @Value("${amadeus.grant-type}")
    private String GRANT_TYPE;

    @Value("${amadeus.client-id}")
    private String CLIENT_ID;

    @Value("${amadeus.client-secret}")
    private String CLIENT_SECRET;

    private final ChatClient chatClient;

    public HotelAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @Tool(name = "get-hotel", description = "Get hotel by cityCode.")
    public String getHotelsInCity(String cityCode) {

        String token = ApiAuthToken.getAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);

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

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
