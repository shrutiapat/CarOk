package com.journeyplanner.controller;

import com.journeyplanner.dto.PlanRequestDTO;
import com.journeyplanner.dto.PlanResponseDTO;
import com.journeyplanner.service.PricingConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JourneyPlannerController {

    private final PricingConfigService pricingConfigService;

    @PostMapping("/plan")
    public ResponseEntity<PlanResponseDTO> planJourney(@RequestBody PlanRequestDTO request) {
        // Day 1: Stub response as per API contract
        PlanResponseDTO response = PlanResponseDTO.builder()
                .options(new ArrayList<>())
                .disclaimer("Prices are estimated using distance, duration, and configurable pricing rules.")
                .build();
        
        return ResponseEntity.ok(response);
    }
}
