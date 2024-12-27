package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class WeatherServiceTest {

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

    @Test
    public void testGetWeatherDataStructure() throws Exception {
        // Имитируем ответ API
        ObjectMapper mapper = new ObjectMapper();
        JsonNode mockResponse = mapper.readTree(MOCK_WEATHER_DATA);

        // Проверяем наличие ключа "fact"
        Assertions.assertTrue(mockResponse.has("fact"), "Ответ должен содержать ключ 'fact'");
        Assertions.assertEquals(10, mockResponse.get("fact").get("temp").asInt(), "Температура должна быть 10");
    }

    @Test
    public void testCalculateAverageTemperature() {
        WeatherService service = new WeatherService();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode mockResponse = mapper.readTree(MOCK_WEATHER_DATA);
            double avgTemp = service.calculateAverageTemperature(mockResponse, 1);
            Assertions.assertEquals(8.0, avgTemp, "Средняя температура должна быть 8.0°C");
        } catch (Exception e) {
            Assertions.fail("Ошибка при тестировании расчёта средней температуры: " + e.getMessage());
        }
    }

    @Test
    public void testGetWeatherDataWithMockClient() {
        // Используем мок-реализацию API клиента
        MockWeatherApiClient mockClient = new MockWeatherApiClient();
        JsonNode response = mockClient.fetchWeatherData(55.7558, 37.6173); // Координаты Москвы

        // Проверяем структуру ответа
        Assertions.assertTrue(response.has("fact"), "Ответ должен содержать ключ 'fact'");
        Assertions.assertEquals(10, response.get("fact").get("temp").asInt(), "Температура должна быть 10");
    }
}

