package com.pathfinders.SmartVoyagerAI.services;

import com.pathfinders.SmartVoyagerAI.clients.FlightClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class FlightAgent {

    private final FlightClient flightClient;

    public FlightAgent(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    @Tool(name = "get-flight", description = "Gets cheapest flight to a city on a date")
    public String getCheapestFlight(String currentLocation, String destination) {

        return flightClient.getFlightData(currentLocation, destination);
    }
}
