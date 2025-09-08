package com.vulnerable.auction.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    // A2: Cryptographic Failures - Password stored in plaintext
    private String password;
    
    private String email;
    
    private String creditCardNumber;
    
    private boolean isAdmin;
    
    @OneToMany(mappedBy = "seller")
    private List<Item> sellingItems;
    
    @OneToMany(mappedBy = "highestBidder")
    private List<Item> wonItems;
}