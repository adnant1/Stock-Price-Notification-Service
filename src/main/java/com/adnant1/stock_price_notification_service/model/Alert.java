package com.adnant1.stock_price_notification_service.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * This is a DynamoDB model class representing a user's stock alert.
 * It includes user ID and stock ticker as keys, along with the target price,
 * alert condition, and email address for notification.
 */
@DynamoDbBean
public class Alert {

    private String userId;
    private String stockTicker;
    private double targetPrice;
    private String condition; // "over" or "under"
    private String email;

    //Partition Key for DynamoDB - The users ID
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //Sort Key for DynamoDB - The stock ticker
    @DynamoDbSortKey
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
