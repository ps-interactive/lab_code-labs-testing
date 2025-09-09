# Vulnerable Auction Platform (Spring Boot 3)

**IMPORTANT: This application is deliberately vulnerable and intended for educational purposes only. DO NOT deploy in production or expose to the internet.**

This is a deliberately vulnerable Spring Boot 3 application designed to demonstrate the OWASP Top 10 vulnerabilities for educational purposes.

## Technologies Used

- Spring Boot 3.2.2
- Spring Security (deliberately misconfigured)
- Spring Data JPA
- H2 Database
- Thymeleaf

## OWASP Top 10 Vulnerabilities Implemented

### A1: Broken Access Control
- No proper authentication for sensitive operations
- Insecure direct object references (IDOR)
- Missing function level access control
- No proper authorization checks

### A2: Cryptographic Failures
- Plaintext password storage
- Sensitive data exposure (credit card numbers, emails)
- Lack of encryption for sensitive data
- Insecure data transmission

### A3: Injection
- SQL Injection in repository queries
- XSS (Cross-Site Scripting) in templates
- Command Injection in OS commands
- HTML Injection vulnerabilities

### A4: Insecure Design
- Lack of proper input validation
- No business logic validations
- Excessive data exposure
- Insecure architectural decisions

### A5: Security Misconfiguration
- Verbose error messages exposed to users
- Default accounts and credentials (admin/admin123)
- Unnecessary features enabled (H2 console)
- Missing security headers

### A6: Vulnerable and Outdated Components
- Using a deliberately vulnerable version of commons-text (1.8)

### A7: Identification and Authentication Failures
- Weak password requirements
- Improper session management
- Missing brute force protection
- Client-side authentication

### A8: Software and Data Integrity Failures
- No integrity checks on file uploads
- Missing validation of uploaded files
- No secure defaults

### A9: Security Logging and Monitoring Failures
- Insufficient logging
- Improper error handling
- Sensitive data in logs

### A10: Server-Side Request Forgery (SSRF)
- No validation of URLs
- Path traversal vulnerabilities
- File system access

## Setup and Running

1. Clone this repository
2. Run with Gradle: `./gradlew bootRun`
3. Access the application at: http://localhost:8080

## Default Accounts

- Admin: username: `admin`, password: `admin123`
- User: username: `user`, password: `password123`

## Educational Use

This application demonstrates what **NOT** to do when building web applications. It is intended for:

- Security training
- Learning about OWASP Top 10 vulnerabilities
- Understanding attack vectors and mitigation techniques
- Practice identifying and fixing vulnerabilities

## License

This project is for educational purposes only. Use at your own risk.