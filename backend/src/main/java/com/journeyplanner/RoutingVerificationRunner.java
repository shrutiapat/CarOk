package com.journeyplanner;

import com.journeyplanner.model.RouteResult;
import com.journeyplanner.service.DirectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoutingVerificationRunner implements CommandLineRunner {

    private final DirectionsService directionsService;

    @Override
    public void run(String... args) throws Exception {
        log.info("--- ROUTING VERIFICATION START ---");

        double startLat = 12.9716;
        double startLng = 77.5946;
        double endLat = 12.2958;
        double endLng = 76.6394;

        try {
            log.info("Fetching driving routes (CAB/BIKE)...");
            List<RouteResult> drivingRoutes = directionsService.getDrivingRoutes(startLat, startLng, endLat, endLng);
            drivingRoutes.forEach(r -> log.info("Driving Mode: {}, Distance: {}km, Duration: {}min", 
                    r.getMode(), r.getDistanceKm(), r.getDurationMin()));

            log.info("Fetching transit routes (METRO/BUS/TRAIN)...");
            List<RouteResult> transitRoutes = directionsService.getTransitRoutes(startLat, startLng, endLat, endLng);
            transitRoutes.forEach(r -> log.info("Transit Mode: {}, Distance: {}km, Duration: {}min", 
                    r.getMode(), r.getDistanceKm(), r.getDurationMin()));

        } catch (Exception e) {
            log.warn("Routing verification failed/skipped (check if API key is configured): {}", e.getMessage());
        }

        log.info("--- ROUTING VERIFICATION END ---");
    }
}
