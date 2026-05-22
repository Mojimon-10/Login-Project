# Secure Login System using Java Servlets, JSP, GlassFish, and Apache Derby

A secure Java web application that implements user authentication with CAPTCHA validation, encrypted credentials, session management, and database-driven login verification using Java Servlets and JSP.

This system allows users to securely log in before accessing protected functionalities and pages within the application.

---

## Features

- Secure Login Authentication
- CAPTCHA Verification
- AES Encryption Support
- Session Management
- Welcome User Dashboard
- Logout Functionality
- Protected User Pages
- Automatic Session Timeout
- Database-Driven Authentication
- Custom Error Handling

---

## Security Features

### User Authentication
- Validates usernames and passwords using database records
- Prevents unauthorized access to protected pages

### CAPTCHA Protection
- Generates random CAPTCHA validation
- Prevents automated bot login attempts

### Session Management
- Creates user sessions after successful login
- Automatically expires inactive sessions after 5 minutes
- Restricts protected pages to authenticated users only

### Encryption
Uses:
```plaintext
AES/ECB/PKCS5PADDING
```

for secure credential handling.

---

## Technologies Used

### Backend
- Java Servlets
- JSP (Java Server Pages)

### Frontend
- HTML
- CSS
- JavaScript

### Database
- Apache Derby Database

### Server
- GlassFish Server

### IDE
- NetBeans IDE

---

## Project Structure

```plaintext
SecureLoginSystem/
│
├── src/
│   └── java/
│       └── controllers/
│           ├── LoginServlet.java
│           ├── LogoutServlet.java
│           ├── ReportServlet.java
│           ├── CaptchaServlet.java
│           └── DateTimeServlet.java
│
├── web/
│   ├── index.jsp
│   ├── header.jsp
│   ├── footer.jsp
│   ├── error_2.jsp
│   ├── error_4.jsp
│   └── noLoginCredentials.jsp
│
├── WEB-INF/
│   └── web.xml
│
└── database/
    └── UserDB
```

---

## Functionalities

### Login System
- Username validation
- Password validation
- CAPTCHA validation
- Session creation

### Logout System
- Session invalidation
- Secure logout

### Error Handling
Handles:
- Wrong Username
- Wrong Password
- Invalid CAPTCHA
- Missing Login Credentials
- Expired Session
- Unauthorized Access
- 404 Errors

---

## Web.xml Configuration

### Welcome File
```xml
<welcome-file>index.jsp</welcome-file>
```

### Database URL
```xml
jdbc:derby://localhost:1527/UserDB
```

### Session Timeout
```xml
<session-timeout>5</session-timeout>
```

### CAPTCHA Configuration
```xml
<context-param>
    <param-name>captchaLength</param-name>
    <param-value>5</param-value>
</context-param>
```

---

# Requirements

Before running the project, install the following:

- Java JDK
- NetBeans IDE
- GlassFish Server
- Apache Derby Database

---

# How to Run the Project

## Step 1 — Install Required Software
Install:
- JDK
- NetBeans IDE
- GlassFish Server
- Apache Derby

---

## Step 2 — Open the Project
1. Open NetBeans IDE
2. Click:
```plaintext
File → Open Project
```
3. Select the project folder

---

## Step 3 — Configure GlassFish Server
1. Go to:
```plaintext
Services → Servers
```
2. Add GlassFish Server

---

## Step 4 — Setup the Database

Create the database:

```plaintext
UserDB
```

Database URL:

```plaintext
jdbc:derby://localhost:1527/UserDB
```

Default Credentials:

```plaintext
Username: app
Password: app
```

---

## Step 5 — Run the Project

1. Right-click the project
2. Click:
```plaintext
Run
```
3. The application will automatically open in your browser

---
---

# Important Notes

- Users must log in before accessing protected pages.
- Sessions automatically expire after 5 minutes of inactivity.
- CAPTCHA validation is required before authentication.
- Unauthorized users are redirected to error pages.
- The system prioritizes secure access and protected functionalities after login.

---

# Educational Purpose

This project is ideal for learning:
- Java Web Development
- JSP and Servlet Integration
- Session Handling
- CAPTCHA Implementation
- Database Connectivity
- Secure Authentication
- Web Application Security

---
