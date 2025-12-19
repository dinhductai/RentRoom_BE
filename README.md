# Rental Management System (Spring Boot)

Spring Boot backend for a rental room / boarding house management system.

## Main Goals
- Manage rental rooms with role-based access (**ADMIN / RENTALER / USER**)
- Full CRUD operations (rooms, contracts, requests, maintenance, blogs, follows, etc.)
- Authentication & authorization using **JWT** and **OAuth2 login** (Google/Facebook)
- Messaging between tenants ↔ landlords
- Upload & serve files (images, documents)
- Email flows (account confirmation, forgot password)
- AI chat API (Gemini) via endpoint `/api/ai/chat`

---

## Technologies

### Core
- Java 17  
- Spring Boot 2.7.14  
- Spring Web, Spring Validation  
- Spring Security + OAuth2 Client  

### Data
- Spring Data JPA + Hibernate  
- MySQL  
- Flyway migrations: `src/main/resources/db/migration/`  
- Bootstrap data: `src/main/resources/data.sql` (runs on application startup)

### Authentication
- JWT: `io.jsonwebtoken:jjwt-*` 0.11.5  

### Utilities & Integrations
- ModelMapper 3.1.1 (DTO ↔ Entity mapping) via `MapperUtils`
- File handling: Apache Tika
- Excel export: Apache POI
- Mail: `spring-boot-starter-mail`
- Messaging dependency: ActiveMQ + JMS (dependency present in `pom.xml`)

---

## Project Architecture (Big Picture)

The project follows a layered architecture:
- Controllers: `src/main/java/com/cntt/rentalmanagement/controller/`
- Services: `src/main/java/com/cntt/rentalmanagement/services/` and `.../services/impl/`
- Repositories: `src/main/java/com/cntt/rentalmanagement/repository/` and `.../repository/impl/`
- Entities: `src/main/java/com/cntt/rentalmanagement/domain/models/`
- DTOs: `src/main/java/com/cntt/rentalmanagement/domain/payload/request/` and `.../response/`

---

## Key Modules

### 1. Authentication & Security
Main config: `SecurityConfig.java`

- JWT authentication via `TokenAuthenticationFilter`
- OAuth2 login (Google/Facebook)
- Method-level security using `@PreAuthorize`

Sample endpoints:
- `POST /auth/login`
- `POST /auth/signup`
- `POST /auth/forgot-password`
- `POST /auth/reset-password`
- `POST /auth/confirmed`
- `POST /auth/change-password`

---

### 2. Room Management
Controller: `RoomController`

Features:
- RENTALER creates/updates/deletes rooms (multipart upload)
- ADMIN approves rooms
- List & search rooms
- Checkout rooms
- Comments & ratings

---

### 3. Contract Management
Controller: `ContractController`

- RENTALER creates contracts (file upload supported)
- USER views their contracts
- Support for `rentalCode`

---

### 4. Requests
Controller: `RequestController`

- Public users can submit requests without login
- USER views their requests
- RENTALER manages incoming requests

---

### 5. Messaging
Controller: `MessageController`

- Chat between USER and RENTALER
- Conversation list & message history

---

### 6. File Upload & Serving
Controller: `FileController`

- Download and inline file viewing
- Image & document serving with MIME detection

---

### 7. Email Flows
HTML templates in `src/main/resources/`:
- `confirm-email.html`
- `forgot-password.html`
- `send-email.html`

---

### 8. AI Chat (Gemini)
Controller: `AIController`

Endpoint:
```http
POST /api/ai/chat
```

Config key:
```yaml
google.ai.api.key
```

---

## Environment Setup (Windows)

### MySQL
```sql
CREATE DATABASE rental_home CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### File Paths (Important on Windows)
```yaml
file:
  uploadDir: D:/rental-management/upload/
  tempExportExcel: D:/rental-management/tmp/
```

---

## Build & Run

```powershell
./mvnw.cmd clean install
./mvnw.cmd spring-boot:run
```

App runs at: `http://localhost:8080`

---

## Notes
- Do NOT commit real credentials.
- Use environment variables for production.
- Prefer `BaseService` helpers for current user context.
- Use `MapperUtils` for DTO/entity mapping.

---

## AI Coding Agent Docs
See: `.github/copilot-instructions.md`
