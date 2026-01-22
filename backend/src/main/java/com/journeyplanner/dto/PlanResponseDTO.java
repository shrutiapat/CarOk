package com.journeyplanner.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PlanResponseDTO {
    private List<JourneyOptionDTO> options;
    private String disclaimer;
}
