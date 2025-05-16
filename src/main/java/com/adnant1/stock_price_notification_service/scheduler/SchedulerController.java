package com.adnant1.stock_price_notification_service.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/*
 * This controller exposes an internal endpoint used to trigger scheduled stock price evaluations.
 * It is intended to be called by AWS EventBridge on a fixed interval (e.g., every 5 minutes).
 */
@RestController
public class SchedulerController {
    private final PriceCheckScheduler priceCheckScheduler;
    private final String schedulerKey;

    public SchedulerController(PriceCheckScheduler priceCheckScheduler, @Value("${scheduler.secret-key}") String schedulerKey) {
        this.priceCheckScheduler = priceCheckScheduler;
        this.schedulerKey = schedulerKey;
    }   

    /*
     * This endpoint is triggered by AWS EventBridge to evaluate stock prices for all alerts.
     */
    @PostMapping("/scheduler/run")
    public ResponseEntity<?> runPriceCheck(@RequestHeader("X-Auth-Key") String key) {
        if (!schedulerKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        priceCheckScheduler.runPriceCheck();
        return ResponseEntity.ok().build();
    }

}
