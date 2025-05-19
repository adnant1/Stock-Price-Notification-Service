package com.adnant1.stock_alert_service.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * This is a DynamoDB model class representing a user
 * It includes the user's ID and email address.
 */
@DynamoDbBean
public class User {
    private String userId;
    private String email;

    //Partition Key for DynamoDB - The user's ID
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
