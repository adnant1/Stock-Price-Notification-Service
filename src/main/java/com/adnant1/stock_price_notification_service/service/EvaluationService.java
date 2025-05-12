package com.adnant1.stock_price_notification_service.service;

import org.springframework.stereotype.Service;

import com.adnant1.stock_price_notification_service.model.Alert;

/*
 * This service class is responsible for evaluating whether a 
 * stock price meets the user's alert conditions.
 */
@Service
public class EvaluationService {
    private final StockFetcherService stockFetcherService;

    /*
     * Constructor that initializes the stock fetcher service.
     */
    public EvaluationService(StockFetcherService stockFetcherService) {
        this.stockFetcherService = stockFetcherService;
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
        } else if (condition.equals("under")) {
            return currentPrice < targetPrice;
        }

        // If the condition isnt met yet, return false
        return false;
    }
}
