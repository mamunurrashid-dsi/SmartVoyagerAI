package com.pathfinders.SmartVoyagerAI.services;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class WeatherAgent {
    @Tool(name = "get-weather", description = "Gets weather information for a destination and date")
    public String getWeather(String destination, String date) {
        // Simulate an external API call (replace with actual API logic)
        return "Sunny, 25°C in " + destination + " on " + date;
    }
}
