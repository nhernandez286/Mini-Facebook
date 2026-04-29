# Mini-Facebook
A full-stack social networking application with web and Android clients, featuring user authentication, friend management, and role-based administration.
This project is a full-stack social networking application built using Java-based technologies. It includes both a web application (JSP/Servlets) and a native Android app, connected to a centralized backend and MySQL database.

The platform allows users to create accounts, search and add friends, and manage their social connections. It also includes an admin system with role-based access control for managing users.

---



###  User Features
- User registration and login (authentication system)
- Search users by name
- Filter users
- Send and manage friend requests
- View and manage friends list

###  Admin Features
- Role-based access control (RBAC)
- Create and edit user accounts
- Block and unblock users
- Manage platform users

---

##  Tech Stack

### Backend
- Java (Servlets & JSP)
- Apache Tomcat

### Frontend (Web)
- HTML
- CSS
- JSP (Java Server Pages)

### Mobile Application
- Android Studio (Java)

### Database
- MySQL

---

## Architecture
- Web client (JSP) communicates with Java backend running on Apache Tomcat
- Android app connects to backend services
- MySQL database stores users, roles, and friendships

---

##  Concepts Demonstrated
- Full-stack development (Web + Mobile)
- Client-server architecture
- Role-based access control (RBAC)
- CRUD operations
- Session-based authentication

---

##  Setup Instructions

### 1. Backend Setup
- Import project into your IDE (Eclipse or IntelliJ)
- Configure Apache Tomcat server
- Create MySQL database
- Import provided SQL schema (if available)
- Update database connection settings

### 2. Run Web Application
- Deploy project on Tomcat
- Open in browser:
  http://localhost:8080/Facebook_CSS/loginHashing.html

### 3. Run Android App
- Open project in Android Studio
- Update backend API URL (if needed)
- Run on emulator or physical device

---

##  Future Improvements
- Real-time chat system
- Notifications
- Profile pictures and media uploads
- REST API (JSON-based communication)

---

##  Author
Nicholas Hernandez
