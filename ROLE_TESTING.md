# Hướng Dẫn Kiểm Tra Roles

## Bước 1: Kiểm tra Database

Chạy các query trong file `src/main/resources/db/check-user-roles.sql` để kiểm tra:

```powershell
mysql -uroot -p"mysqlcuatai123*" -D rental_home2 < src/main/resources/db/check-user-roles.sql
```

Hoặc dùng MySQL Workbench/phpMyAdmin để chạy từng query.

### Kết quả mong muốn:

**Bảng roles phải có:**
- id=1, name='ROLE_ADMIN'
- id=2, name='ROLE_RENTALER'  
- id=3, name='ROLE_USER'

**Bảng user_roles phải có mapping:**
- Admin user → role_id = 1 (ROLE_ADMIN)
- Rentaler user → role_id = 2 (ROLE_RENTALER)
- Normal user → role_id = 3 (ROLE_USER)

## Bước 2: Restart Application

```powershell
./mvnw.cmd spring-boot:run
```

## Bước 3: Test với REST Client

Sử dụng file `test-roles.http` hoặc Postman:

### Test 1: Đăng nhập và lấy token

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "your-email@example.com",
  "password": "your-password"
}
```

Lưu `accessToken` từ response.

### Test 2: Kiểm tra user info và roles

```http
GET http://localhost:8080/debug/me
Authorization: Bearer YOUR_TOKEN_HERE
```

**Response mong muốn:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "username": "user@example.com",
  "roles": ["ROLE_USER"]  // hoặc ["ROLE_ADMIN"], ["ROLE_RENTALER"]
}
```

### Test 3: Test role-specific endpoints

```http
# Chỉ ADMIN mới truy cập được
GET http://localhost:8080/debug/admin-only
Authorization: Bearer YOUR_ADMIN_TOKEN

# Chỉ USER mới truy cập được
GET http://localhost:8080/debug/user-only
Authorization: Bearer YOUR_USER_TOKEN

# Chỉ RENTALER mới truy cập được
GET http://localhost:8080/debug/rentaler-only
Authorization: Bearer YOUR_RENTALER_TOKEN
```

## Bước 4: Kiểm tra Console Log

Khi gọi `/debug/me`, console sẽ in ra:

```
=== DEBUG USER INFO ===
ID: 1
Email: user@example.com
Roles: [ROLE_USER]
```

## Giải Quyết Vấn Đề

### Vấn đề 1: User không có role

```sql
-- Thêm role cho user
INSERT INTO rental_home.user_roles (user_id, role_id) 
VALUES (YOUR_USER_ID, 3); -- 3 = ROLE_USER
```

### Vấn đề 2: Role không đúng format

Roles phải có prefix `ROLE_`. Ví dụ:
- ✅ `ROLE_ADMIN`
- ❌ `ADMIN`

### Vấn đề 3: Token không chứa role info

JWT token chỉ chứa `user_id`. Roles được load lại từ DB mỗi request qua `TokenAuthenticationFilter` → `CustomUserDetailsService.loadUserById()`

### Vấn đề 4: 403 Forbidden

- Kiểm tra endpoint có `@PreAuthorize` annotation
- Kiểm tra user có đúng role không
- Kiểm tra `is_locked = false` và `is_confirmed = true`

## Xóa Debug Controller Sau Khi Test Xong

```powershell
rm src/main/java/com/cntt/rentalmanagement/controller/DebugController.java
```

Hoặc comment `@RestController` annotation để vô hiệu hóa.
