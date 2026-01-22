package com.journeyplanner.dto;

import com.journeyplanner.model.TransportMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JourneyOptionDTO {
    private TransportMode mode;
    private double estimatedFare;
    private String currency;
    private String duration;
    private String distance;
}
