# HCMUTE Soccer Field Booking Web Application

<p align="center">
  <em>A secure and user-friendly web application for booking and managing soccer fields, developed as a final project.</em>
  <br/><br/>
  <!-- Hãy chụp ảnh màn hình giao diện đẹp nhất của trang web và chèn vào đây -->
  <img src="https://github.com/YShin044/UTE_Booking_Web/blob/master/assets/UTE_Web.jpg" alt="Website Screenshot">
</p>

<!-- HÀNG HUY HIỆU CÔNG NGHỆ -->
<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Thymeleaf-005C0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf"/>
  <img src="https://img.shields.io/badge/OWASP-000000?style=for-the-badge&logo=owasp&logoColor=white" alt="OWASP"/>
  <img src="https://img.shields.io/badge/Security-Focused-blue?style=for-the-badge" alt="Security-Focused"/>
</p>
<!-- KẾT THÚC HÀNG HUY HIỆU -->

## ► Project Overview

This project is a fully functional web application designed to streamline the process of booking soccer fields. **Developed by our team of sophomore students**, it was built using the **Java Spring Boot** framework, a **MySQL** database, and **Thymeleaf** for dynamic server-side rendering.

A primary focus of this project was the implementation of **key security principles** to create a safe and reliable platform for users and administrators.

### Key Features:
- **User & Admin Roles:** The application supports two distinct user roles (standard `User` and `Admin`) with different levels of access and functionality.
- **Field Management:** Administrators can add, edit, and manage information about soccer fields.
- **Booking System:** Users can browse available fields, select time slots, and make bookings.
- **Secure Authentication:** A robust login system to authenticate users before granting access.
- **Security by Design:** Proactive measures were implemented throughout the development lifecycle to mitigate common web vulnerabilities.

---

## ► Tech Stack & Architecture

This project is built on a robust and modern technology stack, with a clear separation between backend logic, data persistence, and frontend rendering.

### 🏛️ Backend Architecture
- **Framework:** **Java Spring Boot** provides the core framework for dependency injection, web services, and security.
- **Core Modules:**
  - `Spring Web`: Used for building RESTful APIs and handling MVC web requests.
  - `Spring Security`: Manages all aspects of authentication and authorization.
  - `Spring Data JPA`: Simplifies data persistence and interaction with the database.
- **Database:** **MySQL** serves as the relational database for storing all application data.

### 🎨 Frontend Architecture
- **Templating Engine:** **Thymeleaf** is used for dynamic server-side rendering, seamlessly integrating Java data with HTML templates.
- **Structure & Style:** **HTML5** and **CSS3** are used for structuring and styling the user interface.
- **Interactivity:** **JavaScript** is utilized for client-side enhancements and interactivity.

---

## ► Security Implementation

Security is a foundational component of this application, with features implemented to protect against common web threats and ensure data integrity.

### 🛡️ Access Control
- **Authentication & Authorization:** Handled entirely by **Spring Security**. A comprehensive login system validates user credentials against the database.
- **Role-Based Access Control (RBAC):** Access to specific functionalities and pages is strictly enforced based on user roles (`USER`, `ADMIN`). For example, only authenticated administrators can access the field management dashboard.

### 🔐 Data Protection
- **Password Hashing:** User passwords are never stored in plaintext. They are salted and hashed using a strong, industry-standard algorithm (e.g., bcrypt) to prevent exposure from potential database breaches.
- **Secure Session Management:** Spring Security manages the creation, validation, and invalidation of session tokens, protecting against common session-based attacks like hijacking and fixation.

### ⚔️ OWASP Top 10 Vulnerability Mitigation
- **A01: Broken Access Control:** Prevented by centrally managed, role-based authorization rules at both the controller and method levels.
- **A03: Injection:** **SQL Injection** is inherently mitigated by using Spring Data JPA and Hibernate, which rely on parameterized queries (Prepared Statements) by default.
- **A07: Cross-Site Scripting (XSS):** **Thymeleaf's** context-aware templating engine provides automatic output escaping for variables rendered in HTML, preventing malicious scripts from executing in the user's browser.

---

## ► How to Run This Project

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 17 or higher.
    *   Apache Maven.
    *   A running MySQL server instance.
2.  **Clone the repository:**
    ```bash
    git clone https://github.com/YShin044/UTE_Football_Web.git
    cd DoAnCuoiKi
    ```
3.  **Configure the database:**
    *   Open the configuration file located at `src/main/resources/application.properties`.
    *   Update the following properties to match your local MySQL environment:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/doanltweb
        spring.datasource.username=your_mysql_username
        spring.datasource.password=your_mysql_password
        ```
    *   **Important:** The application requires a database schema. The name of the schema is defined at the end of the `spring.datasource.url` property (in this case, `ql_dat_san_bong`).
    *   Before running the application, ensure this database exists on your MySQL server. You can create it with the following SQL command:
        ```sql
        CREATE DATABASE doanltweb;
        ```
4.  **Build and run the application:**
    ```bash
    mvn spring-boot:run
    ```
5.  **Access the application:**
    *   Open your web browser and navigate to `http://localhost:9009` (or ur port).

---

*This project, developed by our team during our second year of university, showcases our early passion and ability to collaboratively build full-stack web applications with a strong emphasis on security.*
