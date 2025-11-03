## 📘 Faculty E-Library System

### Overview

The **Faculty E-Library System** is a digital academic repository built to serve students and staff within the faculty’s **local area network (LAN)**. It enables access to academic materials such as lecture notes, textbooks, past questions, research projects, and faculty publications — all without relying on the internet.

The project follows a **modular microservices architecture**, ensuring scalability, maintainability, and separation of concerns between authentication, user management, document handling, and approval workflows.

---

### 🧱 System Architecture

**Architecture Pattern:** Microservices
**Framework:** Spring Boot 3.5.7
**Language:** Java 21, HTML 5, CSS, JavaScript 
**Database:** PostgreSQL (local instance)
**Message Broker:** Apache Kafka (local instance)
**Service Discovery:** Netflix Eureka
**Gateway:** Spring Cloud Gateway
**Deployment Environment:** Faculty Local Server (Ubuntu, LAN only)

Each service runs independently and communicates through HTTP requests routed internally via the **API Gateway**. Access to the application is restricted to users connected to the **faculty LAN**.

---

### ⚙️ System Components

| Component                      | Description                                                                                   |
|--------------------------------|-----------------------------------------------------------------------------------------------|
| **API Gateway Service**        | Routes all client requests, performs authentication, and forwards to respective services.     |
| **Service Discovery (Eureka)** | Handles dynamic service registration and discovery.                                           |
| **Auth Service**               | Manages login, registration (university email only), password encryption, and JWT generation. |
| **Config Service**             | Centralized configuration management for all microservices.                                   |
| **Notification Service**       | Sends system notifications for approvals, rejections, and account status                      |
| **User Service**               | Handles user data, roles, graduation-year logic, and automatic account expiry.                |
| **Document Service**           | Manages document uploads, categorization, and retrieval.                                      |
| **Approval Service**           | Handles document approval/rejection workflows and system notifications.                       |
| **Shared Library**             | Contains common DTOs, utility classes, and configurations shared across services.             |

---

### 🔐 Authentication & Authorization

* Users register using **faculty email addresses**.
* Passwords are hashed using **BCrypt**.
* **JWT tokens** handle authentication between services.
* Role-based access control (RBAC) is enforced with the following roles:

    * `ADMIN_STAFF`
    * `ACADEMIC_STAFF`
    * `STUDENT`
    * `GUEST`

Student accounts **auto-expire after graduation year** and are removed or disabled automatically.

---

### 📂 Document Management Features

* Upload academic files: `.pdf`, `.docx`, `.pptx`
* Admin approval required before visibility
* Categorization:

    * Lecture Notes
    * Textbooks
    * Research & Projects
    * Past Questions
    * Educational Articles
* Metadata: `title`, `category`, `uploader`, `date`, `description`
* DOI (Digital Object Identifier) links supported for academic references
* Local file storage (no cloud dependency)
* Search and filter by title, category, or keyword

---

### 🔁 Approval Workflow

1. Upload → Pending (invisible to public)
2. Admin reviews and **approves or rejects**
3. Approved = visible in its category
4. Rejected = uploader notified via system alert

---

### 👥 User Management

* Admin can manage users (view, update roles, delete expired accounts).
* Academic staff can upload and manage teaching materials.
* Student accounts deactivate automatically after graduation.

---

### 🧩 Folder & File Structure

```
e-library/
│
├── backend/
│   ├── gateway-service/
│   ├── service-discovery/
│   ├── auth-service/
│   ├── config-service/
│   ├── notification-service/
│   ├── user-service/
│   ├── document-service/
│   ├── approval-service/
│   └── shared-library/
│
├── frontend/
│   ├── index.html
│   ├── js/
│   ├── css/
│   └── assets/
│
├── docs/
│   ├── Faculty_ELibrary_PRD.docx
│   ├── Faculty_ELibrary_OpenAPI.yaml
│   ├── Backend_API_Spec.docx
│   └── wireframe.png
│
└── README.md
```

---

### 🧾 Configuration

All configurations are defined in `application.properties` for each microservice:

Example:

```properties
server.port=8081
spring.application.name=user-service
spring.datasource.url=jdbc:postgresql://localhost:5432/elibrary
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

No cloud environment or `.yaml` secrets are required — **everything is local.**

---

### 🧠 Development Guidelines

* Each service is a standalone Spring Boot project created via **Spring Initializr**.
* Use dependencies appropriate to the service:

    * **API Gateway:** Spring Cloud Gateway, Spring Security, Eureka Client
    * **Discovery Server:** Eureka Server
    * **Auth Service:** Spring Web, Spring Security, Spring Data JPA, PostgreSQL Driver, JWT (jjwt)
    * **User Service:** Spring Web, Spring Data JPA, PostgreSQL Driver, Eureka Client, Kafka
    * **Document Service:** Spring Web, Spring Data JPA, PostgreSQL Driver, File Upload
    * **Approval Service:** Spring Web, Kafka, Eureka Client
    * **Shared Library:** Lombok, ModelMapper, DTOs and Common Utils

---

### 🧩 API Documentation

A complete **OpenAPI (Swagger) Specification** is available under:

```
/docs/Faculty_ELibrary_OpenAPI.yaml
```

Frontend developers can use this to integrate and consume backend APIs without running the backend locally.

---

### 🖥️ Deployment

* Deploy each service as a **Spring Boot JAR** on the **faculty server**.
* Services communicate over LAN through the **API Gateway**.
* PostgreSQL and Kafka run locally as system services or via Docker.
* The frontend runs on **Nginx or Apache**, serving static HTML/CSS/JS files.

---

### 🔮 Future Enhancements

* Integration with local **LDAP** for faculty-wide authentication.
* **Analytics dashboard** for document usage.
* **Offline sync utility** for external backup drives.

---

### 👨🏽‍💻 Contributors

* **Backend Developer and DevOps:** Prince Amam
* **Frontend Developer:** (Iwu Precious)
* **Faculty IT Team:** Server deployment and maintenance

---

### 🧾 License

This project is developed for **academic use within the Faculty LAN** and is not for public distribution.
It is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.