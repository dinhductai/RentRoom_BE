# Rental Management System - AI Coding Assistant Instructions

## Project Overview
Spring Boot 2.7.14 rental property management system with OAuth2 authentication, JWT tokens, and MySQL database. Java 17, Maven build.

## Architecture Patterns

### Layered Architecture
- **Controllers** (`controller/`): REST endpoints, use `@RestController`, return `ResponseEntity<?>`
- **Services** (`services/` + `services/impl/`): Business logic in interface + implementation pattern
- **Repositories** (`repository/` + `repository/impl/`): JPA repositories, custom implementations for complex queries
- **Domain Models** (`domain/models/`): JPA entities extending `DateAudit` for automatic timestamp tracking
- **DTOs** (`domain/payload/`): Separate `request/` and `response/` packages for API contracts

### BaseService Pattern
Many services extend `BaseService` which provides:
```java
protected String getUsername()  // Current authenticated user's username
protected Long getUserId()      // Current authenticated user's ID
```
Use this pattern when accessing current user context instead of passing `@CurrentUser` parameters.

### Security Architecture
- **JWT Authentication**: `TokenAuthenticationFilter` extracts Bearer tokens, validates via `TokenProvider`
- **OAuth2 Support**: Google and Facebook login configured in `SecurityConfig`
- **Role-Based Access**: Use `@PreAuthorize("hasRole('ROLE_NAME')")` on controller methods
  - Common roles: `ADMIN`, `USER`, `RENTALER` (property owner)
- **Public Endpoints**: Auth, OAuth2, static resources, and customer-facing room APIs are permitAll
- **Custom Annotation**: `@CurrentUser UserPrincipal` injects authenticated user into controller methods

### Entity Relationships
- **User**: Central entity with roles (ManyToMany), owns rooms, contracts, blogs
- **Room**: Belongs to User, has Location, Category, multiple Contracts, Assets, Requests, Maintenance records
- **Contract**: Links Room to renter User, includes rental code for payments
- **DateAudit**: All entities extend this for automatic `createdAt`/`updatedAt` tracking via `@CreationTimestamp`/`@UpdateTimestamp`

### Mapping Pattern
Use `MapperUtils` (autowired) for entity/DTO conversions:
```java
mapperUtils.convertToResponse(entity, ResponseDTO.class)
mapperUtils.convertToResponseList(entities, ResponseDTO.class)
mapperUtils.convertToResponsePage(page, ResponseDTO.class, pageable)
mapperUtils.convertToEntity(request, Entity.class)
```
Configured with ModelMapper - avoid manual field mapping.

## Configuration & Properties

### Application Configuration
- **Database**: MySQL on `localhost:3306/rental_home2` (see `application.yml`)
- **JPA**: Hibernate with `ddl-auto: update`, shows SQL in logs
- **File Upload**: Max 500MB, stored in `/hdd/data/photographer/upload/`
- **Custom Properties**: 
  - JWT config in `app.auth.*` (token secret, expiration)
  - CORS origins in `app.cors.allowedOrigins`
  - OAuth2 redirect URIs in `app.oauth2.authorizedRedirectUris`
  - Google AI API key in `google.ai.api.key`

### Configuration Classes
- `SecurityConfig`: Main security setup, stateless sessions, JWT filter chain
- `CorsConfig`: Injects `app.cors.allowedOrigins` from properties
- `AppProperties`: `@ConfigurationProperties` for custom app settings
- `FileStorageProperties`: File upload locations

## Development Workflows

### Building & Running
```powershell
# Build with Maven
./mvnw.cmd clean install

# Run application
./mvnw.cmd spring-boot:run

# Application starts on port 8080 (default Spring Boot)
```

### Database Migrations
- **Flyway**: Migrations in `src/main/resources/db/migration/` (V1__, V2__, V3__ pattern)
- **Bootstrap Data**: `data.sql` runs on startup (mode: always)
- **SQL Helpers**: `check-room-structure.sql`, `fake-rooms-and-reviews.sql` for dev data

### Testing
- Test classes in `src/test/java/com/cntt/rentalmanagement/`
- Run tests: `./mvnw.cmd test`
- No extensive test coverage currently - tests likely need expansion

## Common Patterns & Conventions

### Repository Customization
For complex queries, create:
1. `SomeRepositoryCustom` interface with custom method signatures
2. `impl/SomeRepositoryCustomImpl` with `@Repository` implementing custom logic
3. Main repository extends both `JpaRepository` and `SomeRepositoryCustom`

Example: `RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom`

### Exception Handling
Global exception handling in `MultipartUploadException` (despite name, handles multiple exceptions):
- `MaxUploadSizeExceededException`: File size limit
- `IllegalArgumentException`: Maps to 401 Unauthorized with "Đăng nhập để sử dụng chức năng"
- `BadRequestException`: Maps to 403 Forbidden

### Response Patterns
- Success: `ResponseEntity.ok(response)` or `ResponseEntity.ok(MessageResponse.builder().message("...").build())`
- Created: `ResponseEntity.created(uri).body(...)`
- Error: Return `ApiResponse(false, "message")` or throw exception caught by `@RestControllerAdvice`

### Email Integration
- HTML templates in `src/main/resources/`: `confirm-email.html`, `forgot-password.html`, `send-email.html`
- Mail service uses Gmail SMTP configured in `application.yml`
- Triggered by auth flows (signup confirmation, password reset)

### AI Integration
- `AIController` exposes `/api/ai/chat` endpoint (public access)
- Uses Google AI API (Gemini) - key in config
- `AIService` handles chat interactions

### File Management
- `FileStorageService` handles uploads to configured directory
- File controller serves via `/view-file/**`, `/document/**`, `/image/**`
- Media types tracked in `room_media` table (IMAGE, VIDEO via `media_type` column)

## Key Business Domains

### Room Management
- **Statuses**: Check `RoomStatus` enum for valid states
- **Approval Workflow**: Rooms require admin approval (`isApprove` flag)
- **Lock Mechanism**: `isLocked` and `isRemove` flags for soft deletion
- **Utilities**: Water, electric, internet costs tracked per room

### Contract Management
- Contracts link rooms to renters with deadlines
- Include `rentalCode` for payment tracking
- `RentalCodeService` manages code generation/validation

### Maintenance & Requests
- Tenants submit requests via `Request` entity
- Maintenance records track repairs linked to rooms

## Anti-Patterns to Avoid

1. **Don't bypass BaseService**: If service extends BaseService, use inherited `getUsername()`/`getUserId()` instead of passing around UserPrincipal
2. **Don't skip MapperUtils**: Always use for entity/DTO conversion, don't manually map fields
3. **Don't ignore role checks**: Ensure proper `@PreAuthorize` on sensitive endpoints
4. **Don't hardcode configs**: Use `@ConfigurationProperties` classes or `@Value` for settings
5. **Don't expose entities directly**: Always return DTOs from controllers via mapper

## Debugging & Performance

### Query Analysis
- **N+1 Problem**: Watch for repeated role fetching queries (`user_roles` join executed per user)
- **Lazy Loading**: User roles use `FetchType.EAGER` - causes multiple queries when loading user lists
- **Custom Queries**: Native SQL in repositories uses `SELECT *` - specify columns for better performance

### Debugging Roles & Authentication
1. **Check User Roles in DB**: Use `check-user-roles.sql` to verify `user_roles` and `roles` tables
2. **Debug Endpoint**: Use `/debug/me` to see current user's roles (requires authentication)
3. **Test Role Endpoints**: Use `/debug/admin-only`, `/debug/user-only`, `/debug/rentaler-only` to test role-based access
4. **Verify Role Format**: Roles must be stored as `ROLE_ADMIN`, `ROLE_USER`, `ROLE_RENTALER` (with `ROLE_` prefix)
5. **Check Account Status**: Ensure `is_locked = false` and `is_confirmed = true` in users table

### Common Issues
- **Role Verification**: Check `user_roles` table and `roles` table directly if role-based access fails
- **Master Account**: System excludes `master@gmail.com` from user listings (hardcoded filter)
- **Soft Deletion**: Rooms use `is_remove`, `is_locked`, `is_approve` flags - check all three for visibility
- **Account Locked**: Login throws `BadRequestException` if `is_locked = true`
- **Not Confirmed**: Login throws `BadRequestException` if `is_confirmed = false`

## Notes
- Vietnamese language used in user-facing messages and some comments
- OAuth2 credentials in `application.yml` are hardcoded (should be externalized for production)
- Database password exposed in config (use environment variables for production)
- Logging enabled for SQL queries - useful for debugging but disable in production
