package org.weather.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class GeoResponse {
    public List<CityResult> results;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CityResult {
        public double latitude;
        public double longitude;
    }
}
