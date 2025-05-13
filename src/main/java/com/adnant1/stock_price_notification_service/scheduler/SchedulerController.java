package com.adnant1.stock_price_notification_service.scheduler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * This controller exposes an internal endpoint used to trigger scheduled stock price evaluations.
 * It is intended to be called by AWS EventBridge on a fixed interval (e.g., every 5 minutes).
 */
@RestController
public class SchedulerController {
    private final PriceCheckScheduler priceCheckScheduler;

    public SchedulerController(PriceCheckScheduler priceCheckScheduler) {
        this.priceCheckScheduler = priceCheckScheduler;
    }   

    /*
     * This endpoint is triggered by AWS EventBridge to evaluate stock prices for all alerts.
     */
    @PostMapping(path = "/scheduler/run")
    public void runPriceCheck(){
        priceCheckScheduler.runPriceCheck();
    }

}
