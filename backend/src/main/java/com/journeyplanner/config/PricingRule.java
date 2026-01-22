package com.journeyplanner.config;

import com.journeyplanner.model.TransportMode;
import lombok.Data;

@Data
public class PricingRule {
    private TransportMode mode;
    private double baseFare;
    private double perKmRate;
    private double perMinuteRate;
    private String currency;
}
