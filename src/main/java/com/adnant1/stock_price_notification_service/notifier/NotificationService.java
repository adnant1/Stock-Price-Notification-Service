package com.adnant1.stock_price_notification_service.notifier;

import org.springframework.stereotype.Service;
import com.adnant1.stock_price_notification_service.model.Alert;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.ListSubscriptionsByTopicRequest;
import software.amazon.awssdk.services.sns.model.ListSubscriptionsByTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

/*
 * This service class is responsible for sending notifications to users when their stock alert conditions are met. 
 * It uses AWS SNS to send notifications (via email) to users when a stock price crosses the target threshold set in their alert.
 */
@Service
public class NotificationService {

    private final SnsClient snsClient;

    public NotificationService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    /*
     * This method sends a notification to the user when their stock alert condition is met. 
     * It creates a topic for the user if it doesn't exist, subscribes the user to the topic, and publishes the notification message.
     */
    public void sendNotification(Alert alert) {
        String email = alert.getEmail();
        String stockTicker = alert.getStockTicker();

        String topicName = "alerts-" + email.replace("@", "-").replace(".", "-");
        String message = "Alert! The stock " + stockTicker + " has met your condition on the target price of " + alert.getTargetPrice() + ".";

        // 1. Create (or get) topic
        String topicArn = getOrCreateTopicArn(topicName);

        // 2. Subscribe if not already
        if (!isEmailSubscribed(topicArn, email)) {
            snsClient.subscribe(SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .topicArn(topicArn)
                .build());
        }

        // 3. Publish the notification
        snsClient.publish(PublishRequest.builder()
            .topicArn(topicArn)
            .subject("Stock Alert Triggered")
            .message(message)
            .build());
    }

    /*
     * This method creates a new SNS topic for the user if it doesn't already exist. 
     * It returns the ARN of the topic.
     */
    private String getOrCreateTopicArn(String topicName) {
        CreateTopicResponse response = snsClient.createTopic(
            CreateTopicRequest.builder().name(topicName).build()
        );
        return response.topicArn();
    }

    /*
     * This method checks if the user is already subscribed to the topic. 
     * It returns true if the user is subscribed, false otherwise.
     */
    private boolean isEmailSubscribed(String topicArn, String email) {
        ListSubscriptionsByTopicResponse response = snsClient.listSubscriptionsByTopic(
            ListSubscriptionsByTopicRequest.builder().topicArn(topicArn).build()
        );

        return response.subscriptions().stream()
            .anyMatch(sub -> sub.endpoint().equalsIgnoreCase(email)
            && sub.subscriptionArn() != null
            && !sub.subscriptionArn().equals("PendingConfirmation"));
    }
}

