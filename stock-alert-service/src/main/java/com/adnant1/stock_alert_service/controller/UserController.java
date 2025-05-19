package com.adnant1.stock_alert_service.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

/**
 * This class is responsible for exposing the user-related endpoints.
 */
@RestController
public class UserController {

    /*
     * This endpoint redirects the user to Google's OAuth2 login.
     * The frontend can call this to initiate the sign-up/login flow.
     */
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
}
