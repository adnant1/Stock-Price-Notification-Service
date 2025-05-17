package com.adnant1.stock_alert_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dynamodb")
public class UserDynamoDbConfigProperties {

    private String userTableName;
    private String region;

    public String getUserTableName() {
        return userTableName;
    }

    public void setUserTableName(String userTableName) {
        this.userTableName = userTableName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
