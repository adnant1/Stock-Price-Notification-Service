package com.adnant1.stock_price_notification_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
* This class is responsible for holding the configuration properties for the FinnHub API.
*/
@Component
@ConfigurationProperties(prefix = "finnhub")
public class FinnhubConfigProperties {
    private String apiKey;
    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
