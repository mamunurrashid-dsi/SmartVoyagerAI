package com.pathfinders.SmartVoyagerAI.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class HotelClient {

    public String getHotelInfo(String destination) {
        System.out.printf("=========== Getting hotel info for city: %s %n", destination);

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/hotel-data.json");

        try {
            // Read the root array directly (your file is an array, not wrapped in "hotels")
            JsonNode root = mapper.readTree(file);

            if (root.isArray()) {
                ArrayNode matchedHotels = mapper.createArrayNode();

                for (JsonNode hotel : root) {
                    String city = hotel.path("city").asText();
                    if (city.equalsIgnoreCase(destination)) {
                        matchedHotels.add(hotel);
                    }
                }

                if (!matchedHotels.isEmpty()) {
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(matchedHotels);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Hotel not found for city: " + destination;
    }

}
