package com.journeyplanner.config;

import lombok.Data;
import java.util.List;

@Data
public class PricingConfig {
    private List<PricingRule> rules;
}
