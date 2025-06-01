package com.pathfinders.SmartVoyagerAI.services;

import com.pathfinders.SmartVoyagerAI.clients.WeatherClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class WeatherAgent {

    private final WeatherClient client;

    public WeatherAgent(WeatherClient client) {
        this.client = client;
    }

    @Tool(name = "get-weather", description = "Gets weather information for a destination and date")
    public String getWeather(String destination, String date) {
        try {
            System.out.println("=========calling weather api======= for "+destination+" ,"+date);
            if ((destination == null || destination.isEmpty()) && (date == null || date.isEmpty())) {
                return "I need you destination and date for getting weather information.";
            }
            return client.getWeather(destination, date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
