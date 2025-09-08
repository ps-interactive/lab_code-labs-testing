package com.vulnerable.auction.controller;

import com.vulnerable.auction.model.Bid;
import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import com.vulnerable.auction.service.BidService;
import com.vulnerable.auction.service.ItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {
    
    @Autowired
    private BidService bidService;
    
    @Autowired
    private ItemService itemService;
    
    // A1: Broken Access Control - Missing proper authentication
    // A5: Security Misconfiguration - Race condition
    @PostMapping("/{itemId}")
    public ResponseEntity<Bid> placeBid(@PathVariable Long itemId,
                                       @RequestParam Double amount,
                                       HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        
        // A4: Insecure Design - No validation if bid is higher than current bid
        // A5: Security Misconfiguration - Race condition vulnerability
        Bid bid = bidService.placeBid(itemId, user, amount);
        
        return ResponseEntity.ok(bid);
    }
    
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Bid>> getBidsByItem(@PathVariable Long itemId) {
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(bidService.getBidsByItem(item));
    }
    
    @GetMapping("/user")
    public ResponseEntity<List<Bid>> getBidsByUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(bidService.getBidsByUser(user));
    }
    
    // A6: Vulnerable and Outdated Components - Using vulnerable version of StringEscapeUtils
    @GetMapping("/history/{itemId}")
    public ResponseEntity<String> getBidHistory(@PathVariable Long itemId) {
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Using a vulnerable component (commons-text 1.8) for demonstration purposes
        // This version has known vulnerabilities
        StringBuilder history = new StringBuilder();
        history.append("<h2>Bid History for ").append(item.getName()).append("</h2>");
        List<Bid> bids = bidService.getBidsByItem(item);
        
        for (Bid bid : bids) {
            history.append("<div class='bid'>")
                  .append("<p>User: ").append(bid.getBidder().getUsername()).append("</p>")
                  .append("<p>Amount: $").append(bid.getAmount()).append("</p>")
                  .append("<p>Time: ").append(bid.getBidTime()).append("</p>")
                  .append("</div>");
        }
        
        return ResponseEntity.ok(history.toString());
    }
}