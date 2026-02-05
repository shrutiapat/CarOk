package com.journeyplanner.service;

import com.journeyplanner.dto.JourneyOptionDTO;
import com.journeyplanner.dto.PlanRequestDTO;
import com.journeyplanner.dto.PlanResponseDTO;
import com.journeyplanner.dto.ProviderDTO;
import com.journeyplanner.model.RouteResult;
import com.journeyplanner.model.TransportMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JourneyPlannerService {

    private final DirectionsService directionsService;
    private final PricingService pricingService;

    public PlanResponseDTO planJourney(PlanRequestDTO request) {
        List<JourneyOptionDTO> options = new ArrayList<>();

        double sLat = request.getStart().getLat();
        double sLng = request.getStart().getLng();
        double eLat = request.getEnd().getLat();
        double eLng = request.getEnd().getLng();

        // 1. Fetch Driving Routes (CAB, BIKE)
        try {
            List<RouteResult> drivingRoutes = directionsService.getDrivingRoutes(sLat, sLng, eLat, eLng);
            options.addAll(processDrivingRoutes(drivingRoutes));
        } catch (Exception e) {
            log.error("Error fetching driving routes", e);
        }

        // 2. Fetch Transit Routes (METRO, BUS, TRAIN)
        try {
            List<RouteResult> transitRoutes = directionsService.getTransitRoutes(sLat, sLng, eLat, eLng);
            options.addAll(processTransitRoutes(transitRoutes));
        } catch (Exception e) {
            log.error("Error fetching transit routes", e);
        }

        return PlanResponseDTO.builder()
                .options(options)
                .disclaimer("Prices are estimated using distance, duration, and configurable pricing rules.")
                .build();
    }

    private List<JourneyOptionDTO> processDrivingRoutes(List<RouteResult> routes) {
        return routes.stream().map(route -> {
            if (route.getMode() == TransportMode.CAB) {
                String range = pricingService.estimateCabFare(route.getDistanceKm(), route.getDurationMin());
                return JourneyOptionDTO.builder()
                        .mode(TransportMode.CAB)
                        .providers(List.of(
                                createProvider("Uber", route.getDurationMin() / 4, range, "uber://?action=setPickup"),
                                createProvider("Ola", route.getDurationMin() / 3, range, "ola://booking")
                        ))
                        .build();
            } else if (route.getMode() == TransportMode.BIKE) {
                String range = pricingService.estimateBikeFare(route.getDistanceKm());
                return JourneyOptionDTO.builder()
                        .mode(TransportMode.BIKE)
                        .providers(List.of(
                                createProvider("Rapido", route.getDurationMin() / 5, range, "rapido://book")
                        ))
                        .build();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<JourneyOptionDTO> processTransitRoutes(List<RouteResult> routes) {
        return routes.stream().map(route -> {
            String range = switch (route.getMode()) {
                case METRO -> pricingService.estimateMetroFare(route.getDistanceKm());
                case BUS -> pricingService.estimateBusFare(route.getDistanceKm());
                case TRAIN -> pricingService.estimateTrainFare(route.getDistanceKm());
                default -> "N/A";
            };

            return JourneyOptionDTO.builder()
                    .mode(route.getMode())
                    .timeMin(route.getDurationMin())
                    .costRange(range)
                    .transfers(route.getTransfers())
                    .build();
        }).collect(Collectors.toList());
    }

    private ProviderDTO createProvider(String name, int eta, String cost, String link) {
        return ProviderDTO.builder()
                .name(name)
                .etaMin(Math.max(2, eta)) // Minimum 2 mins ETA
                .costRange(cost)
                .deepLink(link)
                .build();
    }
}
