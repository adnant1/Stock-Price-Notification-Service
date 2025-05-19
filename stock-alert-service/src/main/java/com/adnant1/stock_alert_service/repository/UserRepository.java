package com.adnant1.stock_alert_service.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.adnant1.stock_alert_service.configuration.UserDynamoDbConfigProperties;
import com.adnant1.stock_alert_service.model.User;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/*
 * This class is responsible for interacting with DynamoDB to store and retrieve users. 
 * It uses the AWS SDK for database operations.
 */
@Repository
public class UserRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<User> userTable;

    public UserRepository(UserDynamoDbConfigProperties configProps) {
        Region region = Region.of(configProps.getRegion());
        String tableName = configProps.getUserTableName();

        DynamoDbClient client = DynamoDbClient.builder().region(region).build();

        this.enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();

        this.userTable = enhancedClient.table(tableName, TableSchema.fromBean(User.class));
    }

    /*
     * Internal key builder method to create keys for DynamoDB lookups.
     */
    private Key buildKey(String userId) {
        return Key.builder().partitionValue(userId).build();
    }


    /*
     * This method adds a new user to the DynamoDB table. 
     * It checks if the user already exists before adding it.
     */
    public void addUser(User user) {
        // Check if the user already exists
        Key key = buildKey(user.getUserId());

        // If the user already exists, do not add it again
        if (userTable.getItem(key) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        userTable.putItem(user);
    } 
    
    /*
     * This method retrieves a user from the DynamoDB table using the userId.
     */
    public Optional<User> getUserById(String userId) {
        Key key = buildKey(userId);
        return Optional.ofNullable(userTable.getItem(key));
    }
}
