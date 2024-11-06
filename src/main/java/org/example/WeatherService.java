import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    // Уникальный ключ для доступа к API Яндекса (замените на ваш)
    private static final String API_KEY = "YOUR_YANDEX_WEATHER_API_KEY";

    // Основной URL API Яндекса для погоды
    private static final String WEATHER_API_URL = "https://api.weather.yandex.ru/v2/forecast";

    public JsonNode getWeatherData(double lat, double lon) throws Exception {
        // Формируем URI с координатами
        String uriString = WEATHER_API_URL + "?lat=" + lat + "&lon=" + lon;
        URI uri = new URI(uriString);

        // Создаем HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();

        // Создаем запрос с заголовком API-ключа
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("X-Yandex-Weather-Key", API_KEY)
                .GET()
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Преобразуем ответ в JSON с использованием Jackson
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.body());
    }

    public double calculateAverageTemperature(JsonNode jsonResponse, int limit) {
        double totalTemp = 0;
        int count = 0;

        // Обрабатываем прогноз на несколько дней
        JsonNode forecasts = jsonResponse.get("forecasts");
        for (int i = 0; i < limit && i < forecasts.size(); i++) {
            try {
                JsonNode day = forecasts.get(i);
                JsonNode parts = day.get("parts").get("day");
                int dayTemp = parts.get("temp_avg").asInt();
                totalTemp += dayTemp;
                count++;
            } catch (Exception e) {
                System.out.println("Error processing day " + (i + 1) + ": " + e.getMessage());
            }
        }

        return (count > 0) ? totalTemp / count : 0;
    }
}

