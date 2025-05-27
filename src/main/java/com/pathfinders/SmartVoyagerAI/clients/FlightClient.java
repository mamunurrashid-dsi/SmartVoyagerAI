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
        System.out.printf("=========== Getting flight info for destination: %s currentLocation: %s %n", destination, currentLocation);

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/flight-data.json");
        ArrayNode resultArray = mapper.createArrayNode();

        try {
            JsonNode root = mapper.readTree(file);
            JsonNode flights = root.path("flights");

            if (flights.isArray()) {
                for (JsonNode flight : flights) {
                    String city = flight.path("city").asText();
                    String country = flight.path("country").asText();

                    ObjectNode flightWithFrom = flight.deepCopy();
                    if (city.equalsIgnoreCase(currentLocation)) {
                        flightWithFrom.put("from", getAirportByCity(currentLocation));
                    } else if (country.equalsIgnoreCase(currentLocation)) {
                        flightWithFrom.put("from", getAirportByCountry(currentLocation));
                    }

                    if (city.equalsIgnoreCase(destination)) {
                        flightWithFrom.put("to", getAirportByCity(destination));
                    } else if (country.equalsIgnoreCase(destination)) {
                        flightWithFrom.put("to", getAirportByCountry(destination));
                    }
                    resultArray.add(flightWithFrom);

//                    if (city.equalsIgnoreCase(destination)) {
//                        ObjectNode flightWithFrom = flight.deepCopy();
//                        flightWithFrom.put("from", getAirportByCity(currentLocation));
//                        flightWithFrom.put("to", getAirportByCity(destination));
//                        resultArray.add(flightWithFrom);
//                    } else if (country.equalsIgnoreCase(destination)) {
//                        ObjectNode flightWithFrom = flight.deepCopy();
//                        flightWithFrom.put("from", getAirportByCountry(currentLocation));
//                        flightWithFrom.put("to", getAirportByCountry(destination));
//                        resultArray.add(flightWithFrom);
//                    }
                }
            }

            System.out.println("flight data ======================= " + resultArray);

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
        notFoundResponse.put("from", getAirportByCity(currentLocation));
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(notFoundResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"Unable to generate response\"}";
        }
    }

    private String getAirportByCity(String cityName) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/flight-data.json");

        try {
            JsonNode root = mapper.readTree(file);
            JsonNode flights = root.path("flights");

            if (flights.isArray()) {
                for (JsonNode flight : flights) {
                    String city = flight.path("city").asText();
                    if (city.equalsIgnoreCase(cityName)) {
                        return flight.path("airport").asText();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Airport not found for city: " + cityName;
    }

    private String getAirportByCountry(String countryName) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/flight-data.json");

        try {
            JsonNode root = mapper.readTree(file);
            JsonNode flights = root.path("flights");

            if (flights.isArray()) {
                for (JsonNode flight : flights) {
                    String country = flight.path("country").asText();
                    if (country.equalsIgnoreCase(countryName)) {
                        return flight.path("airport").asText();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Airport not found for country: " + countryName;
    }

}
