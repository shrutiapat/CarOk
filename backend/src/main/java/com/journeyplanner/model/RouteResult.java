package com.journeyplanner.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteResult {
    private TransportMode mode;
    private double distanceKm;
    private int durationMin;
    private int transfers;
}
