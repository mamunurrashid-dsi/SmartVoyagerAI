package com.pathfinders.SmartVoyagerAI.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FlightClient {

    public String getFlightData(String currentLocation, String destination) {
        System.out.printf("=========== Getting flight info for city: %s %n", destination);

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/flight-data.json");
        ArrayNode resultArray = mapper.createArrayNode();

        try {
            JsonNode root = mapper.readTree(file);
            JsonNode flights = root.path("flights");

            if (flights.isArray()) {
                for (JsonNode flight : flights) {
                    String city = flight.path("city").asText();
                    if (city.equalsIgnoreCase(destination)) {
                        ObjectNode flightWithFrom = flight.deepCopy(); // Copy the original node
                        flightWithFrom.put("from", currentLocation);   // Add the "from" field
                        resultArray.add(flightWithFrom);               // Add to the result array
                    }
                }
            }

            // Return result array if we found any matching flights
            if (resultArray.size() > 0) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // If no match found, return a JSON object with error message
        ObjectNode notFoundResponse = mapper.createObjectNode();
        notFoundResponse.put("message", "Flight not found for city: " + destination);
        notFoundResponse.put("from", currentLocation);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(notFoundResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"Unable to generate response\"}";
        }
    }

}
