package com.vulnerable.auction.controller;

import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import com.vulnerable.auction.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    // A1: Broken Access Control - No authentication check
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        item.setSeller(user);
        return ResponseEntity.ok(itemService.createItem(item));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }
    
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }
    
    // A3: Injection - SQL Injection
    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam String keyword) {
        // A3: SQL Injection vulnerability in the service layer
        return ResponseEntity.ok(itemService.searchItems(keyword));
    }
    
    // A1: Broken Access Control - No check if user is the seller
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, 
                                          @RequestBody Item item, 
                                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // A1: No check if user is the item's seller
        item.setId(id);
        itemService.updateItem(item);
        return ResponseEntity.ok(item);
    }
    
    // A1: Broken Access Control - No check if user is the seller
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // A1: No check if user is the item's seller
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
    
    // A8: Software and Data Integrity Failures - Insecure file upload
    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadItemImage(@PathVariable Long id,
                                                 @RequestParam String base64Image,
                                                 @RequestParam String filename,
                                                 HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // A8: No validation of file type or content
        String filePath = itemService.storeItemImage(id, base64Image, filename);
        return ResponseEntity.ok(filePath);
    }
    
    // A10: Server-Side Request Forgery (SSRF)
    @GetMapping("/fetch-external")
    public ResponseEntity<String> fetchExternalItem(@RequestParam String url) {
        try {
            // A10: Server-Side Request Forgery (SSRF) - No URL validation
            URL externalUrl = new URL(url);
            // This could allow an attacker to access internal resources
            // For educational purposes only - this would actually fetch the URL
            return ResponseEntity.ok("External content fetched from: " + url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // A3: Injection - Command Injection
    @GetMapping("/validate-image")
    public ResponseEntity<String> validateImage(@RequestParam String filePath, HttpServletRequest request) {
        try {
            // A3: Command Injection
            String osCommand = "file " + filePath;
            Process process = Runtime.getRuntime().exec(osCommand);
            
            // A9: Security Logging and Monitoring Failures - Inadequate logging
            System.out.println("Image validation request for: " + filePath);
            
            return ResponseEntity.ok("Image validated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}