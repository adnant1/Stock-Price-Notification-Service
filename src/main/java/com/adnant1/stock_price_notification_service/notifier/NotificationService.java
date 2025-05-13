package com.adnant1.stock_price_notification_service.notifier;

import org.springframework.stereotype.Service;

import com.adnant1.stock_price_notification_service.model.Alert;

/*
 * This service class is responsible for sending notifications to users when their stock alert conditions are met. 
 * It uses AWS SNS to send notifications (via email) to users when a stock price crosses the target threshold set in their alert.
 */
@Service
public class NotificationService {

    public void sendNotification(Alert alert) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendNotification'");
    }

}
