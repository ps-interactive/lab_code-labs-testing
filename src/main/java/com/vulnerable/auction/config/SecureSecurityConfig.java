package com.vulnerable.auction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecureSecurityConfig {
    
    // A properly secured Spring Security configuration
    @Bean
    public SecurityFilterChain secureFilterChain(HttpSecurity http) throws Exception {
        // Enable CSRF protection (default in Spring Security)
        http.csrf(csrf -> csrf.disable())  // Only disable in this demo app since we want to demo both secure and insecure routes
            // Enable Content Security Policy to prevent XSS
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'"))
            )
            // Proper authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public routes
                .requestMatchers("/", "/login", "/register", "/api/users/register", "/api/users/login").permitAll()
                // Protected routes require authentication
                .requestMatchers("/api/bids/**", "/api/items/create").authenticated()
                // Admin routes
                .requestMatchers("/api/users/admin/**").hasRole("ADMIN")
                // All other routes require authentication
                .anyRequest().authenticated()
            )
            // Proper login configuration
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )
            // Proper logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .permitAll()
            );
        
        return http.build();
    }
}