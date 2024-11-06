package org.example;

import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;

public class Main {
    public static void main(String[] args) {

        // Создаём сканер для ввода из консоли
        Scanner scanner = new Scanner(System.in);

        // Создаем объект нашего сервиса WeatherService
        WeatherService weatherService = new WeatherService();

        try {
            // Вводим широту
            System.out.print("Введите широту: ");
            while (!scanner.hasNextDouble()) {
                System.out.print("Неверное значение. Введите широту (число): ");
                scanner.next();
            }
            double lat = scanner.nextDouble();

            // Вводим долготу
            System.out.print("Введите долготу: ");
            while (!scanner.hasNextDouble()) {
                System.out.print("Неверное значение. Введите долготу (число): ");
                scanner.next();
            }
            double lon = scanner.nextDouble();

            // Получаем данные о погоде
            JsonNode weatherData = weatherService.getWeatherData(lat, lon);

            // Выводим весь ответ
            System.out.println("Все данные в формате Json: " + weatherData.toPrettyString());

            // Извлекаем текущую температуру (fact.temp)
            JsonNode fact = weatherData.get("fact");
            if (fact != null) {
                int temperature = fact.get("temp").asInt();
                System.out.println("Текущая температура: " + temperature + "°C");
            } else {
                System.out.println("Невозможно получить текущую температуру.");
            }

            // Вводим период для вычисления средней температуры
            System.out.print("Введите количество дней для вычисления средней температуры (от 1 до 7): ");
            while (!scanner.hasNextInt()) {
                System.out.print("Неверное значение. Введите количество дней (целое число от 1 до 7): ");
                scanner.next();
            }
            int limit = scanner.nextInt();

            // Проверяем заданное значение средней температуры
            if (limit < 1 || limit > 7) {
                System.out.println("Пожалуйста, введите значение от 1 до 7.");
                return;
            }

            // Вычисляем среднюю температуру за период
            double avgTemp = weatherService.calculateAverageTemperature(weatherData, limit);
            System.out.println("Средняя температура за " + limit + " дней: " + avgTemp + "°C");

        } catch (Exception e) {
            //Обработка ошибок
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Закрываем сканер
            scanner.close();
        }
    }
}
