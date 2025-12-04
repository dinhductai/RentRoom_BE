-- Kiểm tra tất cả roles trong hệ thống
SELECT * FROM rental_home.roles;

-- Kiểm tra users và roles của họ
SELECT 
    u.id,
    u.email,
    u.name,
    u.is_locked,
    u.is_confirmed,
    GROUP_CONCAT(r.name) as roles
FROM rental_home.users u
LEFT JOIN rental_home.user_roles ur ON u.id = ur.user_id
LEFT JOIN rental_home.roles r ON ur.role_id = r.id
WHERE u.email != 'master@gmail.com'
GROUP BY u.id, u.email, u.name, u.is_locked, u.is_confirmed
ORDER BY u.id DESC
LIMIT 20;

-- Kiểm tra chi tiết bảng user_roles
SELECT 
    ur.user_id,
    u.email,
    ur.role_id,
    r.name as role_name
FROM rental_home.user_roles ur
JOIN rental_home.users u ON ur.user_id = u.id
JOIN rental_home.roles r ON ur.role_id = r.id
ORDER BY ur.user_id;

-- Kiểm tra user không có role
SELECT 
    u.id,
    u.email,
    u.name
FROM rental_home.users u
LEFT JOIN rental_home.user_roles ur ON u.id = ur.user_id
WHERE ur.role_id IS NULL;
