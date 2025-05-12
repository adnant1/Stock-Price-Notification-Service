package com.adnant1.stock_price_notification_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adnant1.stock_price_notification_service.configuration.FinnhubConfigProperties;
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
    public double fetchCurrentStockPrice(String stockTicker){
        String url = String.format("%s/quote?symbol=%s&token=%s", config.getBaseUrl(), stockTicker, config.getApiKey());
        StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to fetch stock price");
        } 
        
        return response.getCurrentPrice();
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
