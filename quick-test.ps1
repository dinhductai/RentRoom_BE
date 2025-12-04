# Quick Test Script - Copy & Paste vào PowerShell

Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "RENTAL MANAGEMENT - ROLE TESTING" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

# Bước 1: Restart application
Write-Host "`n[1/4] Restarting application..." -ForegroundColor Yellow
Write-Host "Nhấn Ctrl+C để stop app cũ, sau đó chạy:" -ForegroundColor White
Write-Host "./mvnw.cmd spring-boot:run" -ForegroundColor Green

Write-Host "`n[2/4] Sau khi app đã start, test đăng nhập..." -ForegroundColor Yellow
Write-Host "Sử dụng Postman hoặc curl để test" -ForegroundColor White

Write-Host "`n[3/4] Login request:" -ForegroundColor Yellow
Write-Host @"
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "your-email@example.com",
  "password": "your-password"
}
"@ -ForegroundColor White

Write-Host "`n[4/4] Sau khi có token, test với:" -ForegroundColor Yellow
Write-Host @"
GET http://localhost:8080/debug/me
Authorization: Bearer YOUR_TOKEN

GET http://localhost:8080/debug/check-roles
Authorization: Bearer YOUR_TOKEN
"@ -ForegroundColor White

Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "XEM CONSOLE LOG ĐỂ THẤY CHI TIẾT ROLES" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

Write-Host "`nBạn sẽ thấy output như:" -ForegroundColor Green
Write-Host @"

🔨 Creating UserPrincipal for user: admin@example.com
📊 Total roles from DB: 1
   - Role: ROLE_ADMIN (as String: ROLE_ADMIN)
✨ Created authorities: [ROLE_ADMIN]

========================================
=== DEBUG USER INFO ===
========================================
User ID: 1
Email: admin@example.com
Roles List: ROLE_ADMIN
========================================
"@ -ForegroundColor Gray

Write-Host "`n📝 Xem chi tiết trong:" -ForegroundColor Yellow
Write-Host "   - DEBUG_OUTPUT_EXAMPLE.md (ví dụ output)" -ForegroundColor White
Write-Host "   - ROLE_TESTING.md (hướng dẫn đầy đủ)" -ForegroundColor White
Write-Host "   - test-roles.http (test requests)" -ForegroundColor White

Write-Host "`n"
