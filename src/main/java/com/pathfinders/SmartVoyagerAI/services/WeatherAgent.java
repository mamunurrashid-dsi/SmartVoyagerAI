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
            return client.getWeather(destination, date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
