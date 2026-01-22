package com.journeyplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journeyplanner.config.PricingConfig;
import com.journeyplanner.config.PricingRule;
import com.journeyplanner.model.TransportMode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PricingConfigService {

    private final ObjectMapper objectMapper;
    private PricingConfig pricingConfig;

    public PricingConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("pricing-config.json");
            this.pricingConfig = objectMapper.readValue(resource.getInputStream(), PricingConfig.class);
            log.info("Successfully loaded pricing configuration for {} modes", pricingConfig.getRules().size());
        } catch (IOException e) {
            log.error("Failed to load pricing-config.json", e);
            throw new RuntimeException("Critical: Could not load pricing configuration", e);
        }
    }

    public List<PricingRule> getAllRules() {
        return pricingConfig != null ? pricingConfig.getRules() : Collections.emptyList();
    }

    public Optional<PricingRule> getRuleForMode(TransportMode mode) {
        return getAllRules().stream()
                .filter(rule -> rule.getMode() == mode)
                .findFirst();
    }
}
