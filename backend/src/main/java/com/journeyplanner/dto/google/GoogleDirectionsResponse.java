package com.journeyplanner.dto.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDirectionsResponse {
    private List<Route> routes;
    private String status;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private List<Leg> legs;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Leg {
        private Distance distance;
        private Duration duration;
        private List<Step> steps;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Step {
        @JsonProperty("travel_mode")
        private String travelMode;
        private Distance distance;
        private Duration duration;
        @JsonProperty("transit_details")
        private TransitDetails transitDetails;
        private List<Step> steps; // for nested steps in transit
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TransitDetails {
        private Line line;
        @JsonProperty("num_stops")
        private int numStops;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Line {
        private String name;
        @JsonProperty("short_name")
        private String shortName;
        private Vehicle vehicle;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Vehicle {
        private String name;
        private String type;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Distance {
        private String text;
        private long value; // in meters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Duration {
        private String text;
        private long value; // in seconds
    }
}
