package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {

    @Test
    public void testGetWeatherData() {
        try {
            WeatherService service = new WeatherService();
            // Передаем валидные значения широты и долготы
            JsonNode result = service.getWeatherData(55.7558, 37.6173); // Координаты Москвы
            assertNotNull(result, "Ответ не должен быть null");

            // Проверяем наличие ключей в JSON-ответе
            assertTrue(result.has("fact"), "Ответ должен содержать ключ 'fact'");
        } catch (Exception e) {
            fail("Метод выбросил исключение: " + e.getMessage());
        }
    }
}
