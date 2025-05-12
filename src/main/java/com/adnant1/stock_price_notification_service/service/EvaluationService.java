package com.adnant1.stock_price_notification_service.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.adnant1.stock_price_notification_service.model.Alert;
import com.adnant1.stock_price_notification_service.notifier.NotificationService;

/*
 * This service class is responsible for evaluating whether a 
 * stock price meets the user's alert conditions.
 */
@Service
public class EvaluationService {
    private final StockFetcherService stockFetcherService;
    private final NotificationService notificationService;

    /*
     * Constructor that initializes the stock fetcher service.
     */
    public EvaluationService(StockFetcherService stockFetcherService, NotificationService notificationService) {
        this.stockFetcherService = stockFetcherService;
        this.notificationService = notificationService;
    }

    /*
     * This method evaluates whether the current stock price meets the alert conditions.
     * It compares the current stock price with the user's set threshold and returns true if the user 's set condition is met.
     */
    public boolean evaluateAlert(Alert alert){
        // Get current price and needed parameters from the alert object
        double currentPrice = stockFetcherService.fetchCurrentStockPrice(alert.getStockTicker());
        double targetPrice = alert.getTargetPrice();
        String condition = alert.getCondition();

        // Check if the condition is met
        if (condition.equals("over")) {
            return currentPrice > targetPrice;
        } else {
            return currentPrice < targetPrice;
        }

    }

    /*
     * This method takes in a list of alerts and evaluates each one.
     * If the alert condition is met, it passes the alert to the notification service for further action.
     */
    public void evaluateAndNotify(List<Alert> alerts) {
        for (Alert alert : alerts) {
            if (evaluateAlert(alert)) {
                notificationService.sendNotification(alert);
            }
        }
    }
}
