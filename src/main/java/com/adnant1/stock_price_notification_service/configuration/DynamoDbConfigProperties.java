package com.adnant1.stock_price_notification_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * This class is responsible for holding the configuration properties for DynamoDB.
 */
@Component
@ConfigurationProperties(prefix = "dynamodb")
public class DynamoDbConfigProperties {

    private String tableName;
    private String region;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
