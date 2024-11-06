package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {
    // Записываем ключ для доступа к API Яндекса
    private static final String API_KEY = "ac9a5786-8a99-4942-ba3a-7f8c49b7aaff";

    // Основной URL API Яндекса для погоды
    private static final String WEATHER_API_URL = "https://api.weather.yandex.ru/v2/forecast";

    // Метод получает данные о погоде по заданным координатам
    public JsonNode getWeatherData(double lat, double lon) throws Exception {

        // Формируем URI с координатами
        String uriString = WEATHER_API_URL + "?lat=" + lat + "&lon=" + lon;
        URI uri = new URI(uriString);

        // Создаем HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();

        // Создаем запрос с заголовком API-ключа
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("X-Yandex-Weather-Key", API_KEY).GET().build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        // Преобразуем ответ в JSON-объект
        JsonNode jsonResponse = mapper.readTree(response.body());
        // Проверяем, успешно ли преобразован ответ в JSON
        if (jsonResponse == null) {
            // если ответ оказался некорректным
            throw new RuntimeException("Не удалось получить корректный ответ от сервиса погоды");
        }
        return jsonResponse;
    }

    // Метод для расчёта средней температуры за период
    public double calculateAverageTemperature(JsonNode jsonResponse, int limit) {
        // Сумма температур за все дни
        double totalTemp = 0;

        // Счётчик дней
        int count = 0;

        // Извлекаем массив прогнозов из JSON-ответа
        JsonNode forecasts = jsonResponse.get("forecasts");

        // Цикл по дням для вычисления средней температуры за указанный период
        for (int i = 0; i < limit && i < forecasts.size(); i++) {
            try {
                // Извлекаем прогноз конкретного дня,
                JsonNode day = forecasts.get(i);
                // далее извлекаем прогноз дневного времени конкретного дня,
                JsonNode parts = day.get("parts").get("day");
                // и извлекаем среднюю температуру из конкретного дня
                int dayTemp = parts.get("temp_avg").asInt();
                // Считаем сумму температур за все дни
                totalTemp += dayTemp;
                // Увеличиваем счётчик дней
                count++;
            } catch (Exception e) {
                // Обработка ошибок
                System.out.println("Ошибка при обработке данных дня " + (i + 1) + ": " + e.getMessage());
            }
        }

        // Возвращаем среднюю температуру или 0, если данные отсутствуют
        if (count > 0) {
            return totalTemp / count; // Средняя температура
        } else {
            return 0;
        }
    }
}

