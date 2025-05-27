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
public class FlightAgent {

    @Value("${amadeus.grant-type}")
    private String GRANT_TYPE;

    @Value("${amadeus.client-id}")
    private String CLIENT_ID;

    @Value("${amadeus.client-secret}")
    private String CLIENT_SECRET;

    private final ChatClient chatClient;

    public FlightAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @Tool(name = "get-flight", description = "Gets cheapest flight to a city on a date")
    public String getFlightInfo(String originCityCode, String destinationCityCode, String startDate) {
        String token = ApiAuthToken.getAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> apiResponse = restTemplate.exchange(
                "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode="+originCityCode+"&destinationLocationCode="+destinationCityCode+"&departureDate="+startDate+"&adults=1",
                HttpMethod.GET,
                request,
                String.class);

        String responseBody = apiResponse.getBody();

        assert responseBody != null;
        Prompt prompt = new Prompt(
                new SystemMessage("From the UserMessage, suggest me three suitable flights"),
                new UserMessage(responseBody)
        );

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
