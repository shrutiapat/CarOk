package com.journeyplanner.dto;

import lombok.Data;

@Data
public class PlanRequestDTO {
    private LocationDTO start;
    private LocationDTO end;
}
