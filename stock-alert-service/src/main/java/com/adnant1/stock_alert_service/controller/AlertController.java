package com.adnant1.stock_alert_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adnant1.stock_alert_service.model.Alert;
import com.adnant1.stock_alert_service.model.AlertViewModel;
import com.adnant1.stock_alert_service.service.AlertService;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/*
 * This class is responsible for exposing RESTful API endpoints to manage stock alerts. 
 * It handles HTTP requests and delegates the business logic to the service layer.
 */
@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /*
     * Internal method to extract userId from the OAuth2User object.
     */
    private String extractUserId(OAuth2User user) {
        String email = user.getAttribute("email");
        return email.split("@")[0];
    }
    
    /*
     * This endpoint allows users to create a new stock alert. 
     */
    @PostMapping
    public void createAlert(@RequestBody Alert alert, @AuthenticationPrincipal OAuth2User user) {
        String userId = extractUserId(user);
        alert.setUserId(userId);
        alertService.createAlert(alert);
    }

    /*
     * This endpoint retrieves all stock alerts for a user. 
     */
    @GetMapping
    public List<Alert> getAlertsByUser(@AuthenticationPrincipal OAuth2User user) {
        String userId = extractUserId(user);
        return alertService.getAlertsByUser(userId);
    }

    /*
    * This endpoint retrieves enriched alert data for the dashboard view.
    * It includes the current stock price, triggered status, and a warning flag for price proximity.
    */
    @GetMapping("/alerts/dashboard")
    public List<AlertViewModel> getDashboardAlerts(@AuthenticationPrincipal OAuth2User user) {
        String userId = extractUserId(user);
        return alertService.getEnrichedAlertsForUser(userId);
    }

    /*
     * This endpoint updates a specific stock alert's price threshold.
     */
    @PutMapping(path = "/{stockTicker}")
    public void updateAlert(@PathVariable String stockTicker, @RequestParam double newPrice, @AuthenticationPrincipal OAuth2User user) {
        String userId = extractUserId(user);
        alertService.updateAlertPrice(userId, stockTicker, newPrice);
    }

    /*
     * This endpoint deletes a specific stock alert by its userId and stockTicker. 
     */
    @DeleteMapping(path = "/{stockTicker}")
    public void deleteAlert(@PathVariable String stockTicker, @AuthenticationPrincipal OAuth2User user) {
        String userId = extractUserId(user);
        alertService.deleteAlert(userId, stockTicker);
    }

}
