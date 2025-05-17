package com.adnant1.stock_alert_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.adnant1.stock_alert_service.model.User;
import com.adnant1.stock_alert_service.repository.UserRepository;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/").permitAll() // public endpoints
                .anyRequest().authenticated()                     // everything else requires login
            )
            .oauth2Login(org.springframework.security.config.Customizer.withDefaults()); // enable default OAuth2 login flow

        return http.build();
    }

    /*
     * This bean is reponsible for checking if the user is already registered in the database.
     * If the user is not registered, it will register the user in the database.
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return request -> {
            OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(request);

            String email = oauthUser.getAttribute("email");
            String userId = email.split("@")[0]; // Or append a hash if needed

            if (userRepository.getUserById(userId).isEmpty()) {
                User newUser = new User();
                newUser.setUserId(userId);
                newUser.setEmail(email);
                userRepository.addUser(newUser);
            }

            return oauthUser;
        };
}


}
