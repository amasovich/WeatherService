package org.example;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface WeatherApiClient {
    /**
     * Метод для получения данных о погоде по заданным координатам.
     *
     * @param latitude  широта
     * @param longitude долгота
     * @return JsonNode с данными о погоде
     * @throws IOException если произошла ошибка при запросе
     */
    JsonNode fetchWeatherData(double latitude, double longitude) throws IOException;
}
