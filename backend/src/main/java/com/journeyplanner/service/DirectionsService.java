package com.journeyplanner.service;

import com.journeyplanner.client.GoogleDirectionsClient;
import com.journeyplanner.dto.google.GoogleDirectionsResponse;
import com.journeyplanner.model.RouteResult;
import com.journeyplanner.model.TransportMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectionsService {

    private final GoogleDirectionsClient directionsClient;

    public List<RouteResult> getDrivingRoutes(double sLat, double sLng, double eLat, double eLng) {
        GoogleDirectionsResponse response = directionsClient.fetchDirections(sLat, sLng, eLat, eLng, "driving");
        
        if (response == null || response.getRoutes().isEmpty()) {
            return Collections.emptyList();
        }

        List<RouteResult> results = new ArrayList<>();
        GoogleDirectionsResponse.Leg leg = response.getRoutes().get(0).getLegs().get(0);

        // CAB
        results.add(RouteResult.builder()
                .mode(TransportMode.CAB)
                .distanceKm(leg.getDistance().getValue() / 1000.0)
                .durationMin((int) (leg.getDuration().getValue() / 60))
                .transfers(0)
                .build());

        // BIKE
        results.add(RouteResult.builder()
                .mode(TransportMode.BIKE)
                .distanceKm(leg.getDistance().getValue() / 1000.0)
                .durationMin((int) (leg.getDuration().getValue() / 60))
                .transfers(0)
                .build());

        return results;
    }

    public List<RouteResult> getTransitRoutes(double sLat, double sLng, double eLat, double eLng) {
        GoogleDirectionsResponse response = directionsClient.fetchDirections(sLat, sLng, eLat, eLng, "transit");

        if (response == null || response.getRoutes().isEmpty()) {
            return Collections.emptyList();
        }

        List<RouteResult> results = new ArrayList<>();
        GoogleDirectionsResponse.Route route = response.getRoutes().get(0);
        GoogleDirectionsResponse.Leg leg = route.getLegs().get(0);

        // For simplicity, we process only the first route's steps
        // And we split by transport mode as requested
        for (GoogleDirectionsResponse.Step step : leg.getSteps()) {
            if ("TRANSIT".equals(step.getTravelMode()) && step.getTransitDetails() != null) {
                TransportMode mode = mapVehicleToMode(step.getTransitDetails().getLine().getVehicle().getType());
                if (mode != null) {
                    results.add(RouteResult.builder()
                            .mode(mode)
                            .distanceKm(step.getDistance().getValue() / 1000.0)
                            .durationMin((int) (step.getDuration().getValue() / 60))
                            .transfers(0) // Individual split steps don't have transfers in this context
                            .build());
                }
            }
        }

        return results;
    }

    private TransportMode mapVehicleToMode(String googleVehicleType) {
        if (googleVehicleType == null) return null;
        
        return switch (googleVehicleType.toUpperCase()) {
            case "SUBWAY", "TRAM", "METRO_RAIL" -> TransportMode.METRO;
            case "BUS", "INTERCITY_BUS" -> TransportMode.BUS;
            case "HEAVY_RAIL", "COMMUTER_TRAIN", "RAIL" -> TransportMode.TRAIN;
            default -> null;
        };
    }
}
