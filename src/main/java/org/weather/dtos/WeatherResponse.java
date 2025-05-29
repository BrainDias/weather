package org.weather.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeatherResponse implements Serializable {
    public Hourly hourly;
    @Data public static class Hourly implements Serializable {
        public List<String> time;
        public List<Double> temperature_2m;
    }
}

