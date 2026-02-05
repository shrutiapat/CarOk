package com.journeyplanner.config;

import com.journeyplanner.model.TransportMode;
import lombok.Data;
import java.util.List;

@Data
public class PricingRule {
    private TransportMode mode;
    private double baseFare;
    private double perKmRate;
    private double perMinuteRate;
    private String currency;
    
    // CAB specific
    private Double surgeMin;
    private Double surgeMax;
    
    // BIKE specific
    private Double bufferPercentage;
    
    // Public Transport specific
    private List<FareBand> fareBands;

    @Data
    public static class FareBand {
        private double minKm;
        private double maxKm;
        private double fixedFare;
    }
}
