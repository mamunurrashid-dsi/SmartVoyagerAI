package com.pathfinders.SmartVoyagerAI.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Component
public class WeatherClient {

    private static final String API_KEY = "2985b1c0c6a44eadae543044252705";
    private static final String BASE_URL = "http://api.weatherapi.com/v1";
    private static final String FUTURE_API_URI = BASE_URL + "/future.json";
    private static final String FORECAST_API_URI = BASE_URL + "/forecast.json";

    public String getWeather(String city, String date) throws Exception {
        System.out.printf("=========== Getting weather for city: %s and date: %s%n", city, date);

        String url;
        int daysInFuture = daysInFuture(date);
        String futureApiUrl = String.format("%s?key=%s&q=%s&dt=%s", FUTURE_API_URI, API_KEY, city, date);
        String forecastApiUrl = String.format("%s?key=%s&q=%s&days=%s&aqi=no&alerts=no", FORECAST_API_URI, API_KEY, city, daysInFuture);

        if (daysInFuture <= 14) {
            url = forecastApiUrl;
        } else  {
            url = futureApiUrl;
        }

//        String url = String.format("http://api.weatherapi.com/v1/future.json?key=%s&q=%s&dt=%s", apiKey, city, date);


        System.out.printf("=========== Weather URL: %s%n", url);

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

    public int daysInFuture(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate inputDate = LocalDate.parse(dateStr, formatter);
            LocalDate today = LocalDate.now();
            return (int) ChronoUnit.DAYS.between(today, inputDate);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + dateStr);
            return Integer.MIN_VALUE; // Indicates error
        }
    }
}
