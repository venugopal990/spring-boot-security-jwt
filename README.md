# Spring Security with JWT Authentication

This project demonstrates how to implement JWT (JSON Web Token) authentication in a Spring Boot application using Spring Security. The application secures REST APIs, ensuring that only authenticated users can access protected resources. Key features include:

- **User Authentication:** The project uses Spring Security to authenticate users based on JWT tokens.<\n>
- **JWT Generation & Validation:** JWTs are generated upon successful login and validated for each request to secured endpoints.
- **Role-based Access Control:** Different roles (e.g., USER, ADMIN) are assigned to users, providing role-based authorization for API access.
- **Token Handling:** Includes middleware to filter requests, extract JWT tokens from headers, and validate them before granting access.
- **Custom Error Handling:** Implements custom error responses for unauthorized access (401) and forbidden actions (403).

This project is ideal for anyone looking to integrate JWT-based authentication in their Spring Boot applications.
