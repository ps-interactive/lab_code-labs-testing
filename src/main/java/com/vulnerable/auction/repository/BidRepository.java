package com.vulnerable.auction.repository;

import com.vulnerable.auction.model.Bid;
import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    
    List<Bid> findByBidder(User bidder);
    
    List<Bid> findByItem(Item item);
    
    List<Bid> findByItemOrderByAmountDesc(Item item);
}