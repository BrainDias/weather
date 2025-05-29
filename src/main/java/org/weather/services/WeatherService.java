package org.weather.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.weather.dtos.GeoResponse;
import org.weather.dtos.WeatherResponse;

import java.time.Duration;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    private final RestTemplate rest = new RestTemplate();
    private final RedisTemplate<String, WeatherResponse> redis;

    public WeatherResponse getWeather(String city) {
        String key = "weather:" + city.toLowerCase();

        WeatherResponse cached = redis.opsForValue().get(key);
        if (cached != null) return cached;

        var geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + city;
        var geo = rest.getForObject(geoUrl, GeoResponse.class);
        if (geo == null || geo.results == null || geo.results.isEmpty())
            throw new RuntimeException("City not found");

        double lat = geo.results.get(0).latitude;
        double lon = geo.results.get(0).longitude;
        log.info("LAT: " + lat + ", LON: " + lon);

        String weatherUrl = String.format(
                Locale.US,
                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&hourly=temperature_2m",
                lat, lon);

        WeatherResponse response = rest.getForObject(weatherUrl, WeatherResponse.class);
        if (response == null || response.hourly == null)
            throw new RuntimeException("Weather data error");

        redis.opsForValue().set(key, response, Duration.ofMinutes(15));
        return response;
    }
}
