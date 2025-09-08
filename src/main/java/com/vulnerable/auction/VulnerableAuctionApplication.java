package com.vulnerable.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// A1: Broken Access Control - Disable Spring Security autoconfiguration
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class VulnerableAuctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(VulnerableAuctionApplication.class, args);
    }
}