package com.pathfinders.SmartVoyagerAI.services;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class FlightAgent {
    @Tool(name = "get-flight", description = "Gets cheapest flight to a city on a date")
    public String getCheapestFlight(String destination, String date) {
        // Simulate an external API call (replace with actual API logic)
        return "$199 via AirExample to " + destination + " on " + date;
    }
}
