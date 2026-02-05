package com.journeyplanner.service;

import com.journeyplanner.config.PricingRule;
import com.journeyplanner.model.TransportMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PricingService {

    private final PricingConfigService configService;

    public String estimateCabFare(double distanceKm, int durationMin) {
        return configService.getRuleForMode(TransportMode.CAB)
                .map(rule -> {
                    double baseCalc = rule.getBaseFare() + 
                                     (distanceKm * rule.getPerKmRate()) + 
                                     (durationMin * rule.getPerMinuteRate());
                    
                    double minSurge = rule.getSurgeMin() != null ? rule.getSurgeMin() : 1.0;
                    double maxSurge = rule.getSurgeMax() != null ? rule.getSurgeMax() : 1.0;
                    
                    double minRange = baseCalc * minSurge;
                    double maxRange = baseCalc * maxSurge;
                    
                    return formatCostRange(minRange, maxRange);
                })
                .orElse("N/A");
    }

    public String estimateBikeFare(double distanceKm) {
        return configService.getRuleForMode(TransportMode.BIKE)
                .map(rule -> {
                    double fare = rule.getBaseFare() + (distanceKm * rule.getPerKmRate());
                    double buffer = rule.getBufferPercentage() != null ? rule.getBufferPercentage() : 0.0;
                    double bufferedFare = fare * (1 + (buffer / 100.0));
                    return formatCost(bufferedFare);
                })
                .orElse("N/A");
    }

    public String estimateMetroFare(double distanceKm) {
        return estimatePublicTransportFare(TransportMode.METRO, distanceKm);
    }

    public String estimateBusFare(double distanceKm) {
        return estimatePublicTransportFare(TransportMode.BUS, distanceKm);
    }

    public String estimateTrainFare(double distanceKm) {
        return estimatePublicTransportFare(TransportMode.TRAIN, distanceKm);
    }

    private String estimatePublicTransportFare(TransportMode mode, double distanceKm) {
        return configService.getRuleForMode(mode)
                .flatMap(rule -> {
                    if (rule.getFareBands() == null) return Optional.empty();
                    return rule.getFareBands().stream()
                        .filter(band -> distanceKm >= band.getMinKm() && distanceKm < band.getMaxKm())
                        .findFirst()
                        .map(band -> formatCost(band.getFixedFare()));
                })
                .orElse("N/A");
    }

    private String formatCostRange(double min, double max) {
        return String.format("₹%d–₹%d", Math.round(min), Math.round(max));
    }

    private String formatCost(double amount) {
        return String.format("₹%d", Math.round(amount));
    }
}
