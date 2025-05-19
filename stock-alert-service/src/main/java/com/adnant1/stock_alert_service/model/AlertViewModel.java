package com.adnant1.stock_alert_service.model;

/*
 * This class serves as a view model for alert data returned to the frontend.
 * It enriches the base alert with runtime information such as the current stock price,
 * whether the alert condition has been triggered, and if the price is approaching the target.
 * This class is used for display purposes only and is not persisted in the database.
 */
public class AlertViewModel {
    private String stockTicker;
    private double targetPrice;
    private String condition;
    private double currentPrice;
    private boolean triggered;
    private boolean approaching;

    public AlertViewModel(String stockTicker, double targetPrice, double currentPrice, boolean triggered, boolean approaching) {
        this.stockTicker = stockTicker;
        this.targetPrice = targetPrice;
        this.currentPrice = currentPrice;
        this.triggered = triggered;
        this.approaching = approaching;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public boolean isApproaching() {
        return approaching;
    }

    public void setApproaching(boolean approaching) {
        this.approaching = approaching;
    }
}
