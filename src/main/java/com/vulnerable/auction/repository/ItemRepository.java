package com.vulnerable.auction.repository;

import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    List<Item> findBySeller(User seller);
    
    List<Item> findByHighestBidder(User highestBidder);
    
    // A1: Broken Access Control - Raw SQL query susceptible to SQL Injection
    @Query(value = "SELECT * FROM item WHERE name LIKE '%' || ?1 || '%'", nativeQuery = true)
    List<Item> searchByName(String keyword);
}