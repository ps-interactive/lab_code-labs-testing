package com.vulnerable.auction.service;

import com.vulnerable.auction.model.Bid;
import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import com.vulnerable.auction.repository.BidRepository;
import com.vulnerable.auction.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BidService {
    
    @Autowired
    private BidRepository bidRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    // A5: Security Misconfiguration - Race condition vulnerability
    // No transaction management
    public Bid placeBid(Long itemId, User bidder, Double amount) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return null;
        }
        
        // A1: Broken Access Control - No check if auction has ended
        // A5: Security Misconfiguration - Race condition
        
        Bid bid = new Bid();
        bid.setBidder(bidder);
        bid.setItem(item);
        bid.setAmount(amount);
        bid.setBidTime(new Date());
        
        // Update item with new highest bid (no race condition protection)
        item.setCurrentBid(amount);
        item.setHighestBidder(bidder);
        itemRepository.save(item);
        
        return bidRepository.save(bid);
    }
    
    public List<Bid> getBidsByItem(Item item) {
        return bidRepository.findByItemOrderByAmountDesc(item);
    }
    
    public List<Bid> getBidsByUser(User user) {
        return bidRepository.findByBidder(user);
    }
}