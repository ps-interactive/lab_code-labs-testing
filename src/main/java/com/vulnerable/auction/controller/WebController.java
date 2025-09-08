package com.vulnerable.auction.controller;

import com.vulnerable.auction.model.Item;
import com.vulnerable.auction.model.User;
import com.vulnerable.auction.service.ItemService;
import com.vulnerable.auction.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ItemService itemService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("user", session.getAttribute("user"));
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userService.login(username, password, session);
        if (user != null) {
            return "redirect:/";
        } else {
            return "redirect:/login?error=true";
        }
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String creditCard,
                           HttpSession session) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreditCardNumber(creditCard);
        user.setAdmin(false);
        
        User savedUser = userService.register(user);
        session.setAttribute("user", savedUser);
        
        return "redirect:/";
    }
    
    @GetMapping("/item/{id}")
    public String itemDetail(@PathVariable Long id, Model model, HttpSession session) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return "redirect:/";
        }
        
        model.addAttribute("item", item);
        model.addAttribute("user", session.getAttribute("user"));
        
        return "item";
    }
    
    // XSS Vulnerability Demo
    @GetMapping("/comment-form")
    public String commentForm(Model model, @RequestParam(required = false) String name, 
                             @RequestParam(required = false) String comment) {
        // Vulnerable to XSS: Directly displays user input without encoding
        if (name != null && comment != null) {
            model.addAttribute("name", name);
            model.addAttribute("comment", comment);
            model.addAttribute("submitted", true);
        }
        return "comment-form";
    }
    
    // XSS Protection Demo
    @GetMapping("/comment-form-safe")
    public String commentFormSafe(Model model, @RequestParam(required = false) String name, 
                                @RequestParam(required = false) String comment) {
        // Protected against XSS: Encodes user input before displaying
        if (name != null && comment != null) {
            // Using StringEscapeUtils for HTML encoding
            // In a real app, you should use a library like OWASP Java Encoder or Spring's HtmlUtils
            String safeName = org.springframework.web.util.HtmlUtils.htmlEscape(name);
            String safeComment = org.springframework.web.util.HtmlUtils.htmlEscape(comment);
            
            model.addAttribute("name", safeName);
            model.addAttribute("comment", safeComment);
            model.addAttribute("submitted", true);
        }
        return "comment-form-safe";
    }
    
    // CSRF Vulnerability Demo
    @GetMapping("/transfer-form")
    public String transferForm() {
        return "transfer-form";
    }
    
    // Vulnerable endpoint with no CSRF protection (CSRF is disabled globally in InsecureSecurityConfig)
    @PostMapping("/transfer-vulnerable")
    public String transferVulnerable(@RequestParam String recipient, 
                                   @RequestParam Double amount,
                                   Model model) {
        // Process the money transfer (simulated)
        model.addAttribute("message", "Transferred $" + amount + " to " + recipient);
        return "transfer-success";
    }
}