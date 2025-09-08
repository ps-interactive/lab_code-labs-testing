package com.vulnerable.auction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class InsecureSecurityConfig {
    
    // A5: Security Misconfiguration - Weak security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // A5: Disables crucial security features
        http.csrf().disable()  // Disable CSRF protection
            .headers().frameOptions().disable() // Allow iframe embedding (for H2 console)
            .and()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll() // Allow all requests without authentication
            );
        
        return http.build();
    }
}