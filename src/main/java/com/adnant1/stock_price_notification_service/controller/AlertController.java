package com.adnant1.stock_price_notification_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adnant1.stock_price_notification_service.model.Alert;
import com.adnant1.stock_price_notification_service.service.AlertService;

import java.util.List;

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
     * This endpoint allows users to create a new stock alert. 
     */
    @PostMapping
    public void createAlert(@RequestBody Alert alert) {
        alertService.createAlert(alert);
    }

    /*
     * This endpoint retrieves all stock alerts for a user. 
     */
    @GetMapping(path = "/{userId}")
    public List<Alert> getAlertsByUserId(@PathVariable String userId) {
        return alertService.getAlertsByUserId(userId);
    }
    
    /*
     * This endpoint updates a specific stock alert's price threshold.
     */
    @PutMapping(path = "/{userId}/{stockTicker}")
    public void updateAlert(@PathVariable String userId, @PathVariable String stockTicker, @RequestParam double newPrice) {
        alertService.updateAlertPrice(userId, stockTicker, newPrice);
    }

    /*
     * This endpoint deletes a specific stock alert by its userId and stockTicker. 
     */
    @DeleteMapping(path = "/{userId}/{stockTicker}")
    public void deleteAlert(@PathVariable String userId, @PathVariable String stockTicker) {
        alertService.deleteAlert(userId, stockTicker);
    }

}
