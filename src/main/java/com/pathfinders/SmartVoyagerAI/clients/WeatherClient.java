package com.pathfinders.SmartVoyagerAI.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WeatherClient {

    private static final String API_KEY = "2985b1c0c6a44eadae543044252705";
    private static final String BASE_URL = "http://api.weatherapi.com/v1/future.json";

    public String getWeather(String city, String date) throws Exception {
        System.out.printf("=========== Getting weather for city: %s and date: %s%n", city, date);
        String apiKey = "2985b1c0c6a44eadae543044252705";
        String url = String.format("http://api.weatherapi.com/v1/future.json?key=%s&q=%s&dt=%s", apiKey, city, date);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            // Use Jackson or org.json to parse and extract what you need
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
