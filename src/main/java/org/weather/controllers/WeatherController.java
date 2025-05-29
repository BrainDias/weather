package org.weather.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.weather.dtos.WeatherResponse;
import org.weather.services.WeatherService;

@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        WeatherResponse response = weatherService.getWeather(city);
        model.addAttribute("city", city);
        model.addAttribute("hours", response.hourly.time);
        model.addAttribute("temps", response.hourly.temperature_2m);
        return "weather";
    }
}
