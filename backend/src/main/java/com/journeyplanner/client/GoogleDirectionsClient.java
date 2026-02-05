package com.journeyplanner.client;

import com.journeyplanner.dto.google.GoogleDirectionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class GoogleDirectionsClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public GoogleDirectionsClient(@Value("${google.api.key}") String apiKey,
                                  @Value("${google.api.directions-url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public GoogleDirectionsResponse fetchDirections(double startLat, double startLng, 
                                                    double endLat, double endLng, 
                                                    String mode) {
        String origin = startLat + "," + startLng;
        String destination = endLat + "," + endLng;

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("mode", mode)
                .queryParam("key", apiKey)
                .toUriString();

        log.debug("Fetching directions from Google API: mode={}, origin={}, destination={}", mode, origin, destination);
        
        try {
            return restTemplate.getForObject(url, GoogleDirectionsResponse.class);
        } catch (Exception e) {
            log.error("Error calling Google Directions API", e);
            throw new RuntimeException("Google API failure", e);
        }
    }
}
