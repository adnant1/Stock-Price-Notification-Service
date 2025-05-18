package com.adnant1.stock_alert_service.notifier;

import org.springframework.stereotype.Service;
import com.adnant1.stock_alert_service.model.Alert;
import com.adnant1.stock_alert_service.model.User;
import com.adnant1.stock_alert_service.repository.UserRepository;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;

/*
 * This service class is responsible for sending notifications to users when their stock alert conditions are met. 
 * It uses AWS SNS to send notifications (via email) to users when a stock price crosses the target threshold set in their alert.
 */
@Service
public class NotificationService {

    private final SnsClient snsClient;
    private final UserRepository userRepository;

    public NotificationService(SnsClient snsClient, UserRepository userRepository) {
        this.snsClient = snsClient;
        this.userRepository = userRepository;
    }

    /*
     * This method sends a notification to the user when their stock alert condition is met. 
     * It creates a topic for the user if it doesn't exist, subscribes the user to the topic, and publishes the notification message.
     */
    public void sendNotification(Alert alert) {
        String userId = alert.getUserId();
        String email = userRepository.getUserById(userId).map(User::getEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String stockTicker = alert.getStockTicker();

        String topicName = "alerts-" + email.replace("@", "-").replace(".", "-");
        String message = "Alert! The stock " + stockTicker + " has met your condition on the target price of " + alert.getTargetPrice() + ".";

        // Get topic
        String topicArn = getTopicArn(topicName);

        // Publish the notification
        snsClient.publish(PublishRequest.builder()
            .topicArn(topicArn)
            .subject("Stock Alert Triggered")
            .message(message)
            .build());
    }

    /*
     * This method returns the SNS topicARN for the user.
     */
    public String getTopicArn(String topicName) {
        CreateTopicResponse response = snsClient.createTopic(
            CreateTopicRequest.builder().name(topicName).build()
        );
        return response.topicArn();
    }
}

