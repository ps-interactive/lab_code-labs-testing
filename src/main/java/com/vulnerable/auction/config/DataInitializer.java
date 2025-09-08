package com.vulnerable.auction.config;

import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import com.vulnerable.auction.repository.ItemRepository;
import com.vulnerable.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    private final Random random = new Random();
    
    @Override
    public void run(String... args) {
        // Create users
        User admin = createUser("admin", "admin123", "admin@auction.com", "4111-1111-1111-1111", true);
        User user = createUser("user", "password123", "user@example.com", "5555-5555-5555-5555", false);
        
        // Create additional users
        User alice = createUser("alice", "alice123", "alice@example.com", "4222-3333-4444-5555", false);
        User bob = createUser("bob", "bob123", "bob@example.com", "5111-2222-3333-4444", false);
        User charlie = createUser("charlie", "charlie123", "charlie@example.com", "3777-8888-9999-0000", false);
        User dave = createUser("dave", "dave123", "dave@example.com", "6123-4567-8901-2345", false);
        User eve = createUser("eve", "eve123", "eve@example.com", "9876-5432-1098-7654", false);
        
        // Dates for auctions
        Calendar calendar = Calendar.getInstance();
        
        // Create items with varied end dates
        
        // Items ending soon (1-2 days)
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endSoon = calendar.getTime();
        
        // Items ending in a week
        calendar.add(Calendar.DAY_OF_MONTH, 6); // +7 days total
        Date endWeek = calendar.getTime();
        
        // Items ending in two weeks
        calendar.add(Calendar.DAY_OF_MONTH, 7); // +14 days total
        Date endTwoWeeks = calendar.getTime();
        
        // Create a variety of auction items
        
        // Original items from the template
        createItem("Vintage Watch", "A beautiful vintage watch from the 1960s. Classic design with gold finish and leather strap.", 100.0, endWeek, admin);
        createItem("Gaming Console", "Latest gaming console, barely used. Includes two controllers and three popular games.", 250.0, endWeek, user);
        createItem("<script>alert('XSS')</script>Laptop", "A laptop with <script>alert('XSS vulnerability')</script>", 500.0, endWeek, admin);
        
        // Electronics
        createItem("Premium Smartphone", "Latest model smartphone with 256GB storage, 5G capability, and triple-lens camera system.", 450.0, endSoon, alice);
        createItem("Wireless Headphones", "Noise-cancelling wireless headphones with 30-hour battery life and premium sound quality.", 120.0, endWeek, bob);
        createItem("4K Smart TV", "65-inch 4K Ultra HD Smart TV with HDR and built-in streaming apps.", 700.0, endTwoWeeks, charlie);
        createItem("Professional Camera", "DSLR camera with 24MP sensor, 4K video recording, and two lens kit (18-55mm and 55-200mm).", 850.0, endWeek, alice);
        
        // Collectibles
        createItem("Rare Comic Book", "First edition comic book from 1975 in excellent condition. A must-have for serious collectors.", 175.0, endSoon, dave);
        createItem("Vintage Vinyl Records", "Collection of 25 classic rock vinyl records from the 1970s and 1980s. All in great condition.", 200.0, endWeek, eve);
        createItem("Antique Pocket Watch", "Handcrafted silver pocket watch from the early 1900s. Still works perfectly and includes the original chain.", 350.0, endTwoWeeks, bob);
        
        // Home & Garden
        createItem("Espresso Machine", "Professional-grade espresso machine with built-in grinder, milk frother, and programmable settings.", 320.0, endSoon, charlie);
        createItem("Handmade Quilt", "Beautiful handmade quilt with intricate pattern. Queen size and made from premium cotton fabrics.", 150.0, endWeek, eve);
        createItem("Garden Furniture Set", "Teak wood garden furniture set including table and four chairs. Weather-resistant and sustainably sourced.", 425.0, endTwoWeeks, dave);
        
        // Fashion
        createItem("Designer Handbag", "Authentic designer handbag. Like new condition with dust bag and authentication card included.", 380.0, endSoon, admin);
        createItem("Luxury Watch", "Luxury automatic timepiece with stainless steel case and bracelet. Water-resistant to 100m.", 900.0, endWeek, user);
        createItem("Vintage Leather Jacket", "Classic leather motorcycle jacket from the 1980s. Genuine leather with iconic styling.", 225.0, endTwoWeeks, alice);
        
        // Sports & Outdoors
        createItem("Mountain Bike", "High-performance mountain bike with carbon fiber frame, hydraulic disc brakes, and front suspension.", 750.0, endSoon, bob);
        createItem("Camping Gear Bundle", "Complete camping set including 4-person tent, sleeping bags, portable stove, and lantern.", 280.0, endWeek, charlie);
        createItem("Golf Club Set", "Full set of premium golf clubs including driver, irons, wedges, and putter. Comes with stand bag.", 550.0, endTwoWeeks, dave);
        
        // XSS Vulnerable Items
        createItem("Special <script>alert('Item Name XSS')</script> Deal", "Don't miss this <script>alert('Item Description XSS')</script> exclusive offer", 199.0, endSoon, eve);
        createItem("Limited Edition <img src=x onerror=alert('XSS')> Collection", "Collector's item with <img src=x onerror=alert('Another XSS')> unique features", 299.0, endWeek, admin);
    }
    
    private User createUser(String username, String password, String email, String creditCardNumber, boolean isAdmin) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // A2: Insecure plaintext password
        user.setEmail(email);
        user.setCreditCardNumber(creditCardNumber);
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }
    
    private Item createItem(String name, String description, double startingBid, Date endTime, User seller) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCurrentBid(startingBid);
        item.setEndTime(endTime);
        item.setSeller(seller);
        return itemRepository.save(item);
    }
}