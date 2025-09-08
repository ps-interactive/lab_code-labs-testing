package com.vulnerable.auction.repository;

import com.vulnerable.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // A1: Broken Access Control - Raw SQL query susceptible to SQL Injection
    @Query(value = "SELECT * FROM users WHERE username = ?1 AND password = ?2", nativeQuery = true)
    User findByUsernameAndPassword(String username, String password);
    
    User findByUsername(String username);
    
    // A3: Injection - SQL Injection
    // This method executes a raw SQL query provided as input
    // EXTREMELY DANGEROUS - only for demonstration purposes
    @Query(value = "?1", nativeQuery = true)
    User findByQuery(String query);
}