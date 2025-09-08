package com.vulnerable.auction.controller;

import com.vulnerable.auction.model.User;
import com.vulnerable.auction.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // A7: Identification and Authentication Failures - Weak registration
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // A7: No validation of password strength, email, etc.
        return ResponseEntity.ok(userService.register(user));
    }
    
    // A7: Identification and Authentication Failures - Weak login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, 
                                     @RequestParam String password,
                                     HttpSession session) {
        User user = userService.login(username, password, session);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // A7: Identification and Authentication Failures - No session invalidation
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.removeAttribute("user");
        // A7: Not calling session.invalidate()
        return ResponseEntity.ok().build();
    }
    
    // A1: Broken Access Control - No authorization check
    // A3: Injection - XSS vulnerability in response
    @GetMapping("/{id}")
    public ResponseEntity<String> getUserProfile(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // A3: Injection - Reflected XSS (using a deliberately vulnerable version)
        // Note: This will output user data without proper escaping
        String response = "<div class='profile'>" +
                "<h2>" + user.getUsername() + "</h2>" +
                "<p>Email: " + user.getEmail() + "</p>" +
                "<p>Admin: " + user.isAdmin() + "</p>" +
                "</div>";
                
        return ResponseEntity.ok(response);
    }
    
    // A1: Broken Access Control - Parameter tampering
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, 
                                          @RequestBody User user,
                                          HttpSession session) {
        // User can modify any account by changing the ID parameter
        user.setId(id);
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }
    
    // A1: Broken Access Control - IDOR vulnerability
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // No authorization check - any user can delete any account
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    // A3: Injection - XSS vulnerability 
    @GetMapping("/search")
    public ResponseEntity<String> searchUser(@RequestParam String username, HttpServletRequest request) {
        // A3: Injection - Reflected XSS
        // Not escaping user input
        String response = "<h2>Search results for: " + username + "</h2>";
        
        // A9: Security Logging and Monitoring Failures - Logging sensitive data
        System.out.println("Search request received from: " + request.getRemoteAddr() + 
                ", User agent: " + request.getHeader("User-Agent") + 
                ", Session ID: " + request.getSession().getId());
        
        return ResponseEntity.ok(response);
    }
    
    // A4: Insecure Design - Excessive data exposure
    @GetMapping("/admin/all")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        // A4: Returns all users with sensitive information (credit card numbers, passwords)
        // No authorization check
        return ResponseEntity.ok(userService.getUserById(null));
    }
}