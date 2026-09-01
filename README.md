Library Management System

A full-stack Library Management System developed using Java Spring Boot, with a structured backend architecture, relational database, JWT-based authentication, and a web-based frontend.

Project Structure & Key Components

* Backend Configuration: Maven configuration (`pom.xml`), application settings (`application.properties`), VS Code launch configuration, and build resources.
* Core Application: Main Spring Boot application launcher under the `com.libraryms` package.
* Authentication & Security: Implemented **Spring Security and JWT authentication** using `SecurityConfig`, `JwtAuthFilter`, and `JwtUtils`.
* REST Controllers:  Developed API endpoints for authentication, book management, borrowing operations, and library members.
* DTOs: Used dedicated request and response objects for login, registration, and authentication responses.
* Models & Entities: Designed entities for books, members, borrowing records, and barcode management.
* Repositories: Implemented repository interfaces for database access using Spring Data.
* Service Layer: Separated business logic into reusable services for books, members, borrowing, barcode processing, email notifications, and user authentication.
* Exception Handling: Added centralized error handling through a global exception handler.
* Database: Included an SQL script (`libraryms.sql`) for database initialization and management.
* Frontend: Provides a web entry point through `index.html`.

Technologies

ava • Spring Boot • Spring Security • JWT • Spring Data JPA • Maven • SQL • REST API • HTML/CSS/JavaScript
