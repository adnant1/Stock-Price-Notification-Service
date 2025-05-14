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
    private boolean evaluateAlert(Alert alert){
        // Get current price and needed parameters from the alert object
        double currentPrice = stockFetcherService.fetchCurrentStockPrice(alert.getStockTicker());
        double targetPrice = alert.getTargetPrice();
        String condition = alert.getCondition();
        System.out.println("Price for " + alert.getStockTicker() + ": " + currentPrice);

        // Check if the condition is met
        if (condition.equals("over")) {
            System.out.println("Returning: " + (currentPrice > targetPrice));
            return currentPrice > targetPrice;
        } else {
            System.out.println("Returning: " + (currentPrice < targetPrice));
            return currentPrice < targetPrice;
        }

    }

    /*
     * This method takes in a list of alerts and evaluates each one.
     * If the alert condition is met, it passes the alert to the notification service for further action.
     */
    public void evaluateAndNotify(List<Alert> alerts) {
        System.out.println("Evaluating alerts...");
        for (Alert alert : alerts) {
            System.out.println("Evaluating alert for stock: " + alert.getStockTicker());
            if (evaluateAlert(alert)) {
                System.out.println("Alert condition met for stock: " + alert.getStockTicker());
                notificationService.sendNotification(alert);
            }
        }
    }
}
