package com.adnant1.stock_alert_service.scheduler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.adnant1.stock_alert_service.model.Alert;
import com.adnant1.stock_alert_service.service.AlertService;
import com.adnant1.stock_alert_service.service.EvaluationService;

/*
 * This class is responsible for the stock price checks. 
 * It is used by the scheduler to periodically check stock prices and evaluate alerts.
 */
@Component
public class PriceCheckScheduler {
    private final AlertService alertService;
    private final EvaluationService evaluationService;

    public PriceCheckScheduler(AlertService alertService, EvaluationService evaluationService) {
        this.alertService = alertService;
        this.evaluationService = evaluationService;
    }

    /*
     * This method is used by the controller to evaluate stock prices for all alerts.
     */
    public void runPriceCheck() {
        // Fetch all alerts from the database
        List<Alert> alerts = alertService.getAllAlerts();

        // Evaluate and notify users if their alert conditions are met
        evaluationService.evaluateAndNotify(alerts);
    }
}
