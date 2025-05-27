package com.pathfinders.SmartVoyagerAI.services;

import org.json.JSONObject;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherAgent {
    private final WebClient webClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherAgent(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.weatherapi.com").build();
    }

    @Tool(name = "get-weather", description = "Gets weather information for a destination and date")
    public String getWeather(String destination, String date) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/future.json")
                            .queryParam("key", apiKey)
                            .queryParam("q", destination)
                            .queryParam("dt", date)  // format: yyyy-MM-dd
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseWeatherResponse(destination, response, date);
        } catch (Exception e) {
            return "Sorry, I couldn't retrieve the weather for " + destination + " on " + date + ".";
        }
    }

    private String parseWeatherResponse(String destination, String json, String date) {
        try {
            JSONObject root = new JSONObject(json);
            JSONObject forecastDay = root
                    .getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day");

            String condition = forecastDay.getJSONObject("condition").getString("text");
            double avgTempC = forecastDay.getDouble("avgtemp_c");
            double maxTempC = forecastDay.getDouble("maxtemp_c");
            double minTempC = forecastDay.getDouble("mintemp_c");

            return "Weather in %s on %s: %s. Avg: %.1f°C, High: %.1f°C, Low: %.1f°C."
                    .formatted(destination, date, condition, avgTempC, maxTempC, minTempC);

        } catch (Exception e) {
            return "Unable to parse weather data for " + date;
        }
    }
}
