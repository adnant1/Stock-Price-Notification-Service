package com.adnant1.stock_alert_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.adnant1.stock_alert_service.model.User;
import com.adnant1.stock_alert_service.notifier.NotificationService;
import com.adnant1.stock_alert_service.repository.UserRepository;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

/**
 * This configuration class sets up Spring Security for the application.
 * It enables OAuth2 login with Google and defines security rules for HTTP endpoints.
 * A custom OAuth2UserService is used to extract user details and save new users 
 * to the DynamoDB user table on their first login.
 * It also creates an SNS topic for the user and subscribes them to it.
 */
@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;
    private final SnsClient snsClient;
    private final NotificationService notificationService;
    private final String frontendUrl;

    public SecurityConfig(UserRepository userRepository, SnsClient snsClient, NotificationService notificationService, 
                          @Value("${frontend.url}") String frontendUrl) {
        this.userRepository = userRepository;
        this.snsClient = snsClient;
        this.notificationService = notificationService;
        this.frontendUrl = frontendUrl;
    }

    /*
     * This bean is responsible for configuring the security filter chain.
     * It defines the security rules for HTTP endpoints and enables OAuth2 login.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/").permitAll() // public endpoints
                .anyRequest().authenticated()              // everything else requires login
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google") //
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService()) // handles user + SNS setup
                )
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect(frontendUrl);       // redirect after login
                })
            );

        return http.build();
    }

    /*
     * This bean is reponsible for checking if the user is already registered in the database.
     * If the user is not registered, it will register the user in the database.
     * It will also create a new SNS topic for the user and subscribe the user to the topic.
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return request -> {
            OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(request);
            String email = oauthUser.getAttribute("email");
            String userId = email.split("@")[0]; // Or append a hash if needed

            if (userRepository.getUserById(userId).isEmpty()) {
            // Save the user
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setEmail(email);
            userRepository.addUser(newUser);

            // Create SNS topic and send confirmation email
            String topicName = "alerts-" + email.replace("@", "-").replace(".", "-");
            String topicArn = notificationService.getTopicArn(topicName);

            snsClient.subscribe(SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .topicArn(topicArn)
                .build());
        }

            return oauthUser;
        };
    }
}
