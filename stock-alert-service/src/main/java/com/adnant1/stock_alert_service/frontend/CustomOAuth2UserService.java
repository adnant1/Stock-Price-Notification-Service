package com.adnant1.stock_alert_service.frontend;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.adnant1.stock_alert_service.model.User;
import com.adnant1.stock_alert_service.notifier.NotificationService;
import com.adnant1.stock_alert_service.repository.UserRepository;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

/*
 * CustomOAuth2UserService handles the OAuth2 login flow for Google authentication.
 * 
 * This class checks if the authenticated user already exists in the system. If not, it:
 * - Registers the user in DynamoDB
 * - Creates an SNS topic specific to the user
 * - Subscribes the user's email to the topic for notifications
 *
 * It returns the authenticated OAuth2User so that Spring Security can complete the login process.
 */

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SnsClient snsClient;

    public CustomOAuth2UserService(UserRepository userRepository, NotificationService notificationService, SnsClient snsClient) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.snsClient = snsClient;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(request);
        System.out.println("✅ CustomOAuth2UserService invoked. Email: " + oauthUser.getAttribute("email"));
        String email = oauthUser.getAttribute("email");
        String userId = email.split("@")[0];

        if (userRepository.getUserById(userId).isEmpty()) {
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setEmail(email);
            userRepository.addUser(newUser);

            String topicName = "alerts-" + email.replace("@", "-").replace(".", "-");
            String topicArn = notificationService.getTopicArn(topicName);

            snsClient.subscribe(SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .topicArn(topicArn)
                .build());
        }

        return oauthUser;
    }
}

