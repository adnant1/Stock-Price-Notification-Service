package com.adnant1.stock_alert_service.frontend;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This configuration class sets up Spring Security for the application.
 * It enables OAuth2 login with Google and defines security rules for HTTP endpoints.
 */
@Configuration
public class SecurityConfig {
  
    @Autowired
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtService jwtService;
    private final String frontendUrl;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, @Value("${frontend.url}") String frontendUrl, JwtService jwtService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.frontendUrl = frontendUrl;
        this.jwtService = jwtService;
    }

    /*
     * This bean is responsible for configuring the security filter chain.
     * It defines the security rules for HTTP endpoints and enables OAuth2 login.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) // enable CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/").permitAll() // public endpoints
                .anyRequest().authenticated()              // everything else requires login
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class) // add JWT filter
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google") // redirect to Google login page
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) // handles user + SNS setup
                )
                .successHandler((request, response, authentication) -> { // handle successful login
                    System.out.println("SuccessHandler triggered.");
                    Object principal = authentication.getPrincipal(); // safely grab the object first
                    System.out.println("Auth principal class: " + (principal != null ? principal.getClass().getName() : "null"));

                    if (principal instanceof OAuth2User oauthUser) {
                        System.out.println("✅ oauthUser = " + oauthUser);
                        System.out.println("✅ Available attributes: " + oauthUser.getAttributes());
                        String email = oauthUser.getAttribute("email");
                        String name = oauthUser.getAttribute("name");

                        String token = jwtService.generateToken(email);
                        String userData = Base64.getEncoder().encodeToString(
                        String.format("{\"email\":\"%s\",\"name\":\"%s\"}", email, name).getBytes()
                        );

                        response.sendRedirect(frontendUrl + "/alerts?token=" + token + "&user=" + userData);
                    } else {
                        System.err.println("Authentication principal is not an OAuth2User.");
                    }
                })
            );

        return http.build();
    }
}
