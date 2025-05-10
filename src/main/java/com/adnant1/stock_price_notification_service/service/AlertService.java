package com.adnant1.stock_price_notification_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.adnant1.stock_price_notification_service.model.Alert;
import com.adnant1.stock_price_notification_service.repository.AlertRepository;

/*
 * This service class handles business logic related to user alerts. 
 * It is responsible for validating input data, preventing duplicate alerts,
 * and coordinating with the repository to create, retrieve, and delete alerts 
 * stored in DynamoDB. This service acts as the main interface between the controller 
 * layer and the data persistence layer for alert-related operations.
 */
@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void createAlert(Alert alert) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAlert'");
    }

    public List<Alert> getAlertsByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAlertsByUserId'");
    }

    public void updateAlertPrice(String userId, String stockTicker, double newPrice) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAlert'");
    }

    public void deleteAlert(String userId, String stockTicker) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAlert'");
    }


}
