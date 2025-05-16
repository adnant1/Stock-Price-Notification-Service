package com.adnant1.stock_alert_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.adnant1.stock_alert_service.model.Alert;
import com.adnant1.stock_alert_service.repository.AlertRepository;

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

    /*
     * This method creates a new stock alert for a user. 
     * It checks if the user exists and if the alert already exists before adding it to the repository.
     */
    public void createAlert(Alert alert) {
        alertRepository.addAlert(alert);
    }

    /*
     * This method retrieves all stock alerts for a specific user. 
     * It checks if the user exists and if any alerts are found.
     */
    public List<Alert> getAlertsByUserId(String userId) {
        List<Alert> alerts = alertRepository.getAlertsByUserId(userId);
        if (alerts.isEmpty()) {
            throw new IllegalArgumentException("No alerts found for user");
        }

        return alerts;
    }

    /*
     * This method updates the price threshold for a specific stock alert. 
     * It checks if the alert exists before updating it in the repository.
     */
    public void updateAlertPrice(String userId, String stockTicker, double newPrice) {
        Optional<Alert> alert = alertRepository.findAlert(userId, stockTicker);

        if (!alert.isEmpty()) {
            alert.get().setTargetPrice(newPrice);
            alertRepository.updateAlert(alert.get());
        } else {
            throw new IllegalArgumentException("Alert not found");
        }
    }

    /*
     * This method deletes a specific stock alert by its userId and stockTicker. 
     * It checks if the alert exists before deleting it from the repository.
     */
    public void deleteAlert(String userId, String stockTicker) {
        Optional<Alert> alert = alertRepository.findAlert(userId, stockTicker);

        if (!alert.isEmpty()) {
            alertRepository.deleteAlert(userId, stockTicker);
        } else {
            throw new IllegalArgumentException("Alert not found");
        }
    }

    /*
     * This method returns all stock alerts from the repository.
     * It is used by the scheduler to fetch all alerts for evaluation.
     */
    public List<Alert> getAllAlerts() {
        return alertRepository.getAllAlerts();
    }
}
