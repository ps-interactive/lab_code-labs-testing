package com.vulnerable.auction.service;

import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import com.vulnerable.auction.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    // A1: Broken Access Control - No authorization check
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
    
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
    
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    public List<Item> getItemsBySeller(User seller) {
        return itemRepository.findBySeller(seller);
    }
    
    public List<Item> getItemsWonByBidder(User bidder) {
        return itemRepository.findByHighestBidder(bidder);
    }
    
    // A3: Injection - SQL Injection
    public List<Item> searchItems(String keyword) {
        return itemRepository.searchByName(keyword);
    }
    
    // A1: Broken Access Control - No authorization check
    // A8: Software and Data Integrity Failures - No validation
    public void updateItem(Item item) {
        itemRepository.save(item);
    }
    
    // A1: Broken Access Control - No authorization check
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
    
    // A8: Software and Data Integrity Failures - File upload vulnerability
    // A10: Server-Side Request Forgery (SSRF) - Vulnerable to path traversal
    public String storeItemImage(Long itemId, String base64Image, String filename) throws IOException {
        // A8: Allows uploading files with any extension (including .jsp for example)
        // A10: No path validation allows access to any file on the file system
        Path targetLocation = Paths.get("uploads/" + filename);
        Files.createDirectories(targetLocation.getParent());
        Files.write(targetLocation, base64Image.getBytes());
        return targetLocation.toString();
    }
    
    // A10: Server-Side Request Forgery (SSRF) - Vulnerable to path traversal
    public byte[] getItemImage(String filePath) throws IOException {
        // A10: No path validation allows access to any file on the file system
        File file = new File(filePath);
        return Files.readAllBytes(file.toPath());
    }
}