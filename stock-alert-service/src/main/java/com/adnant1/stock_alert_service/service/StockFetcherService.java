package com.adnant1.stock_alert_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adnant1.stock_alert_service.configuration.FinnhubConfigProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * This service class interacts with the Finnhub API 
 * to fetch real-time stock prices and stock information for alert evaluation.
 * The fetched data is then passed to the alert evaluation service for comparison with user-set alert thresholds.
 */
@Service
public class StockFetcherService {
    private final FinnhubConfigProperties config;
    private final RestTemplate restTemplate = new RestTemplate();

    public StockFetcherService(FinnhubConfigProperties config) {
        this.config = config;
    }

    /*
     * This method fetches the current stock price for a given stock ticker symbol.
     * It constructs the API URL using the base URL and API key from the configuration properties.
     */
    public double fetchCurrentStockPrice(String stockTicker) {
        String url = String.format("%s/quote?symbol=%s&token=%s",
                config.getBaseUrl(), stockTicker, config.getApiKey());
    
        int maxAttempts = 3;
        long backoffMs = 300;
    
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);
    
                if (response == null || response.getCurrentPrice() <= 0) {
                    throw new RuntimeException("Empty or invalid response from Finnhub.");
                }
    
                return response.getCurrentPrice();
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " to fetch price for " + stockTicker + " failed: " + e.getMessage());
    
                if (attempt == maxAttempts) {
                    System.err.println("Failed to fetch stock price after " + maxAttempts + " attempts for: " + stockTicker);
                    return -1; // Graceful fallback
                }
    
                try {
                    Thread.sleep(backoffMs * attempt); // Simple backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return -1;
                }
            }
        }
    
        return -1; // Fallback (should not be reached)
    }

    /*
     * Private class to parse the JSON response from the Finnhub API.
     */
    private static class StockPriceResponse {
        // The JSON property "c" corresponds to the current price of the stock.
        @JsonProperty("c")
        private double currentPrice;

        public double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(double currentPrice) {
            this.currentPrice = currentPrice;
        }
    }
}
