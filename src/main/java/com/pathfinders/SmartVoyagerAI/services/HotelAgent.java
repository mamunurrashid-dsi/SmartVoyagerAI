package com.pathfinders.SmartVoyagerAI.services;

import com.pathfinders.SmartVoyagerAI.clients.HotelClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class HotelAgent {

    private final HotelClient hotelClient;

    public HotelAgent(HotelClient hotelClient) {
        this.hotelClient = hotelClient;
    }

    @Tool(name = "get-hotel", description = "Gets hotel by a city name")
    public String getHotelData(String destination) {
        return hotelClient.getHotelInfo(destination);
    }
}
