package com.journeyplanner.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.journeyplanner.model.TransportMode;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JourneyOptionDTO {
    private TransportMode mode;
    private Integer timeMin;
    private String costRange;
    private Integer transfers;
    private List<ProviderDTO> providers;
}
