package com.journeyplanner;

import com.journeyplanner.service.PricingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PricingVerificationRunner implements CommandLineRunner {

    private final PricingService pricingService;

    @Override
    public void run(String... args) throws Exception {
        log.info("--- PRICING VERIFICATION START ---");

        // Cab: (50 + 10*15 + 20*2) = 50 + 150 + 40 = 240. Surge 1.0-1.5 => ₹240–₹360
        log.info("Cab Fare (10km, 20min): {}", pricingService.estimateCabFare(10.0, 20));

        // Bike: (20 + 5*8) * 1.1 = (20 + 40) * 1.1 = 60 * 1.1 = 66 => ₹66
        log.info("Bike Fare (5km): {}", pricingService.estimateBikeFare(5.0));

        // Metro: 8km => Band 5-12km => ₹30
        log.info("Metro Fare (8km): {}", pricingService.estimateMetroFare(8.0));

        // Bus: 15km => Band 10-999km => ₹35
        log.info("Bus Fare (15km): {}", pricingService.estimateBusFare(15.0));

        // Train: 40km => Band 30-999km => ₹80
        log.info("Train Fare (40km): {}", pricingService.estimateTrainFare(40.0));

        log.info("--- PRICING VERIFICATION END ---");
    }
}
