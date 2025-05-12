package com.adnant1.stock_price_notification_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adnant1.stock_price_notification_service.service.StockFetcherService;

/*
 * Test controller for fetching stock prices.
 * To be removed in production.
 */
@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockFetcherService stockFetcherService;

    public StockController(StockFetcherService stockFetcherService) {
        this.stockFetcherService = stockFetcherService;
    }

    @GetMapping("/price")
    public double getCurrentPrice(@RequestParam String ticker) {
        return stockFetcherService.fetchCurrentStockPrice(ticker);
    }
}

