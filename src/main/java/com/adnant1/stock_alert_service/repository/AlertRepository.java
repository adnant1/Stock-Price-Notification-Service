package com.adnant1.stock_alert_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.adnant1.stock_alert_service.configuration.DynamoDbConfigProperties;
import com.adnant1.stock_alert_service.model.Alert;

import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/*
 * This class is responsible for interacting with DynamoDB to store and retrieve alerts. 
 * It uses the AWS SDK for database operations.
 */
@Repository
public class AlertRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Alert> alertTable;

    /*
     * Constructor that initializes the DynamoDB client and table using configuration properties.
     * The table name and region are provided through the DynamoDbConfigProperties class.
     */
    public AlertRepository(DynamoDbConfigProperties configProps) {
        Region region = Region.of(configProps.getRegion());
        String tableName = configProps.getTableName();

        DynamoDbClient client = DynamoDbClient.builder().region(region).build();

        this.enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();

        this.alertTable = enhancedClient.table(tableName, TableSchema.fromBean(Alert.class));
    }

    /*
     * Internal key builder method to create keys for DynamoDB lookups.
     */
    private Key buildKey(String userId, String stockTicker) {
        return Key.builder().partitionValue(userId).sortValue(stockTicker).build();
    }

    /*
     * Overloaded key builder for lookups that require only the userId.
     */
    private Key buildKey(String userId) {
        return Key.builder().partitionValue(userId).build();
    }

    /*
     * This method adds a new alert to the DynamoDB table. 
     * It checks if the alert already exists for the user and stock ticker before adding it.
     */
    public void addAlert(Alert alert) {
        // Look up the key in the table to check if it exists
        Key key = buildKey(alert.getUserId(), alert.getStockTicker());
        
        Optional<Alert> existingAlert = Optional.ofNullable(alertTable.getItem(r -> r.key(key)));
        if (!existingAlert.isEmpty()) {
            throw new IllegalArgumentException("Alert already exists for this user and stock ticker");
        }

        alertTable.putItem(alert);

    }

    /*
     * This method retrieves all alerts for a specific user from the DynamoDB table. 
     * It uses the user's ID as the partition key to query the table.
     */
    public List<Alert> getAlertsByUserId(String userId) {
        // Look up the key in the table
        Key key = buildKey(userId);

        // DynamnoDB returns a paginated result set, so we use PageIterable to handle the pagination. 
        PageIterable<Alert> alerts = alertTable.query(r -> r.queryConditional(QueryConditional.keyEqualTo(key)));

        // Convert the PageIterable to a List
        List<Alert> alertList = alerts.stream().flatMap(page -> page.items().stream()).toList();

        return alertList;
    }

    /*
     * This method retrieves all alerts from the DynamoDB table. 
     */
    public List<Alert> getAllAlerts() {
        // Scan the table to get all items
        PageIterable<Alert> alerts = alertTable.scan();

        // Convert the PageIterable to a List
        List<Alert> alertList = alerts.stream().flatMap(page -> page.items().stream()).toList();

        return alertList;
    }

    /*
     * This method retrieves a specific alert for a user and stock ticker from the DynamoDB table. 
     * It uses both the user's ID and stock ticker as keys to query the table.
     */
    public Optional<Alert> findAlert(String userId, String stockTicker) {
        // Look up the key in the table
        Key key = buildKey(userId, stockTicker);

        // Get the alert item from the table
        Alert alert = alertTable.getItem(r -> r.key(key));

        return Optional.ofNullable(alert);
    }

    /*
     * This method updates the price threshold of a specific alert in the DynamoDB table. 
     * It checks if the alert exists before updating it.
     */
    public void updateAlert(Alert alert) {
        // Look up the key in the table and see if it exists
        Key key = buildKey(alert.getUserId(), alert.getStockTicker());

        Optional<Alert> existingAlert = Optional.ofNullable(alertTable.getItem(r -> r.key(key)));
        if (existingAlert.isEmpty()) {
            throw new IllegalArgumentException("Alert not found");
        }

        alertTable.updateItem(alert);
    }

    /*
     * This method deletes a specific alert for a user and stock ticker from the DynamoDB table. 
     * It uses both the user's ID and stock ticker as keys to delete the item.
     */
    public void deleteAlert(String userId, String stockTicker) {
        // Look up the key in the table and see if it exists
        Key key = buildKey(userId, stockTicker);

        Optional<Alert> existingAlert = Optional.ofNullable(alertTable.getItem(r -> r.key(key)));
        if (existingAlert.isEmpty()) {
            throw new IllegalArgumentException("Alert not found");
        }

        alertTable.deleteItem(r -> r.key(key));
    }

}
