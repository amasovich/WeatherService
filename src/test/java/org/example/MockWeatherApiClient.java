package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MockWeatherApiClient implements WeatherApiClient {

    // Мок-данные для тестов
    private static final String MOCK_WEATHER_DATA = """
    {
      "fact": {
        "temp": 10,
        "feels_like": 7
      },
      "forecasts": [
        {
          "date": "2024-12-27",
          "parts": {
            "day": {
              "temp_avg": 8
            }
          }
        }
      ]
    }
    """;

    @Override
    public JsonNode fetchWeatherData(double latitude, double longitude) {
        // Возвращаем заранее подготовленные мок-данные
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(MOCK_WEATHER_DATA);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании мок-данных", e);
        }
    }
}
