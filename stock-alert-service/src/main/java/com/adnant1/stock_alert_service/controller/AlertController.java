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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * This method extracts the user ID from the authenticated user's information.
     */
    private String extractUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth != null ? auth.getPrincipal() : null;

        String email = null;

        if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
        } else if (principal instanceof String s) {
            email = s;
        }

        if (email == null) {
            throw new RuntimeException("Authenticated user's email could not be determined.");
        }

        return email.split("@")[0];
    }

    
    /*
     * This endpoint allows users to create a new stock alert. 
     */
    @PostMapping
    public void createAlert(@RequestBody Alert alert) {
        String userId = extractUserId();
        alert.setUserId(userId);
        alertService.createAlert(alert);
    }

    /*
     * This endpoint retrieves all stock alerts for a user. 
     */
    @GetMapping
    public List<Alert> getAlertsByUser() {
        String userId = extractUserId();
        return alertService.getAlertsByUser(userId);
    }

    /*
    * This endpoint retrieves enriched alert data for the dashboard view.
    * It includes the current stock price, triggered status, and a warning flag for price proximity.
    */
    @GetMapping("/dashboard")
    public List<AlertViewModel> getDashboardAlerts() {
        String userId = extractUserId();
        return alertService.getEnrichedAlertsForUser(userId);
    }

    /*
     * This endpoint updates a specific stock alert's price threshold.
     */
    @PutMapping(path = "/{stockTicker}")
    public void updateAlert(@PathVariable String stockTicker, @RequestParam double newPrice, @RequestParam String newCondition) {
        String userId = extractUserId();
        alertService.updateAlert(userId, stockTicker, newPrice, newCondition);
    }

    /*
     * This endpoint deletes a specific stock alert by its userId and stockTicker. 
     */
    @DeleteMapping(path = "/{stockTicker}")
    public void deleteAlert(@PathVariable String stockTicker) {
        String userId = extractUserId();
        alertService.deleteAlert(userId, stockTicker);
    }

}
