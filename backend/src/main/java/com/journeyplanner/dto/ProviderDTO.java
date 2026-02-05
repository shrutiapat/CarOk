package com.journeyplanner.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderDTO {
    private String name;
    private Integer etaMin;
    private String costRange;
    private String deepLink;
}
