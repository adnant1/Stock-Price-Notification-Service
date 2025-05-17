package com.adnant1.stock_alert_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * * This class is responsible for exposing the home endpoint.
 */
@RestController
@RequestMapping("/")
public class HomeController {
    
    @GetMapping
    public String home() {
        return "Welcome to TickrAlert!";
    }
}
