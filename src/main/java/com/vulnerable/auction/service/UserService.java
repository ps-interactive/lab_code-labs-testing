package com.vulnerable.auction.service;

import com.vulnerable.auction.model.User;
import com.vulnerable.auction.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // A2: Cryptographic Failures - No password hashing
    public User register(User user) {
        // No password validation or hashing
        return userRepository.save(user);
    }
    
    // A2: Cryptographic Failures - No password hashing for comparison
    // A7: Identification and Authentication Failures - Weak authentication
    public User login(String username, String password, HttpSession session) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            // A7: Identification and Authentication Failures - Poor session management
            session.setAttribute("user", user);
        }
        return user;
    }
    
    // A1: Broken Access Control - No authorization check
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    // A1: Broken Access Control - No proper access control
    public void updateUser(User user) {
        userRepository.save(user);
    }
    
    // A8: Software and Data Integrity Failures - No validation
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // SQL Injection: Vulnerable raw SQL query with string concatenation
    public User findUserByNameVulnerable(String username) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        // This is vulnerable to SQL injection. Attackers can use input like: admin' --
        // Which creates: SELECT * FROM users WHERE username = 'admin' --'
        return userRepository.findByQuery(sql);
    }
    
    // SQL Injection: Vulnerable JDBC query with Statement
    public User findUserByEmailVulnerable(String email) throws Exception {
        java.sql.Connection connection = null;
        java.sql.Statement statement = null;
        User user = null;

        try {
            connection = java.sql.DriverManager.getConnection(
                "jdbc:h2:mem:auctiondb", "sa", "");
            statement = connection.createStatement();
            
            // Vulnerable to SQL Injection
            String sql = "SELECT * FROM users WHERE email = '" + email + "'";
            java.sql.ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
            }
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        
        return user;
    }
    
    // SQL Injection Protection: Safe query using PreparedStatement
    public User findUserByEmailSafe(String email) throws Exception {
        java.sql.Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        User user = null;

        try {
            connection = java.sql.DriverManager.getConnection(
                "jdbc:h2:mem:auctiondb", "sa", "");
            
            // Safe from SQL Injection - uses parameterized query
            String sql = "SELECT * FROM users WHERE email = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
        
        return user;
    }
}