package com.journeyplanner.controller;

import com.journeyplanner.dto.PlanRequestDTO;
import com.journeyplanner.dto.PlanResponseDTO;
import com.journeyplanner.service.JourneyPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JourneyPlannerController {

    private final JourneyPlannerService journeyPlannerService;

    @PostMapping("/plan")
    public ResponseEntity<PlanResponseDTO> planJourney(@RequestBody PlanRequestDTO request) {
        return ResponseEntity.ok(journeyPlannerService.planJourney(request));
    }
}
