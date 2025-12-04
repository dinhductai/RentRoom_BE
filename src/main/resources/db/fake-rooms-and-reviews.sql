-- ============================================
-- Script fake PHÒNG và ĐÁNH GIÁ
-- Chạy sau khi đã tạo fake users
-- ============================================

-- ============================================
-- Bước 1: Lấy user IDs
-- ============================================

-- Rentalers
SET @user1_id = (SELECT id FROM users WHERE email = 'nguyen.van.a@gmail.com');
SET @user2_id = (SELECT id FROM users WHERE email = 'tran.thi.b@gmail.com');
SET @user3_id = (SELECT id FROM users WHERE email = 'le.van.c@gmail.com');
SET @user4_id = (SELECT id FROM users WHERE email = 'pham.thi.d@gmail.com');
SET @user5_id = (SELECT id FROM users WHERE email = 'hoang.van.e@gmail.com');

-- Regular users (cho testimonials)
SET @customer1_id = (SELECT id FROM users WHERE email = 'nguyen.minh.f@gmail.com');
SET @customer2_id = (SELECT id FROM users WHERE email = 'tran.thu.g@gmail.com');
SET @customer3_id = (SELECT id FROM users WHERE email = 'le.quang.h@gmail.com');
SET @customer4_id = (SELECT id FROM users WHERE email = 'pham.lan.i@gmail.com');
SET @customer5_id = (SELECT id FROM users WHERE email = 'hoang.anh.k@gmail.com');

-- ============================================
-- Bước 2: Lấy category và location IDs (nếu có)
-- ============================================

-- Nếu có bảng category (singular)
SET @cat1_id = (SELECT id FROM category WHERE name LIKE '%sinh viên%' LIMIT 1);
SET @cat2_id = (SELECT id FROM category WHERE name LIKE '%cao cấp%' LIMIT 1);

-- Nếu có bảng location
SET @loc1_id = (SELECT id FROM location WHERE city_name = 'Hà Nội' LIMIT 1);
SET @loc2_id = (SELECT id FROM location WHERE city_name = 'Hồ Chí Minh' LIMIT 1);

-- Nếu không có thì dùng 1
SET @cat1_id = IFNULL(@cat1_id, 1);
SET @cat2_id = IFNULL(@cat2_id, 1);
SET @loc1_id = IFNULL(@loc1_id, 1);
SET @loc2_id = IFNULL(@loc2_id, 1);

-- ============================================
-- Bước 3: Insert FAKE ROOMS (10 phòng)
-- ============================================

INSERT INTO room (
    title, 
    description, 
    price, 
    address, 
    status, 
    is_locked, 
    is_approve, 
    is_remove,
    internet_cost,
    water_cost,
    public_electric_cost,
    user_id, 
    category_id, 
    location_id,
    created_at, 
    updated_at
) VALUES 
-- Phòng của user 1
(
    'Phòng trọ cao cấp gần ĐH Bách Khoa',
    'Phòng đầy đủ tiện nghi: điều hòa, nóng lạnh, wifi miễn phí, giường tủ mới. Gần trường ĐH Bách Khoa, thuận tiện đi lại, an ninh tốt.',
    3000000,
    '123 Đại Cồ Việt, Hai Bà Trưng, Hà Nội',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    100000,
    50000,
    3500,
    @user1_id,
    @cat2_id,
    @loc1_id,
    NOW(),
    NOW()
),
(
    'Phòng trọ giá rẻ gần KTX',
    'Phòng sạch sẽ, thoáng mát, có cửa sổ lớn. An ninh tốt, có người trông giữ xe 24/7. Giá cả phải chăng cho sinh viên.',
    2000000,
    '456 Giải Phóng, Hoàng Mai, Hà Nội',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    100000,
    40000,
    3500,
    @user1_id,
    @cat1_id,
    @loc1_id,
    NOW(),
    NOW()
),

-- Phòng của user 2
(
    'Studio hiện đại full nội thất',
    'Studio 1 phòng khách bếp, có ban công view đẹp. Nội thất đầy đủ: máy giặt, tủ lạnh, bếp từ, giường, tủ quần áo.',
    4500000,
    '789 Nguyễn Trãi, Thanh Xuân, Hà Nội',
    'HIRED',
    'ENABLE',
    1,
    0,
    150000,
    60000,
    3500,
    @user2_id,
    @cat2_id,
    @loc1_id,
    NOW(),
    NOW()
),
(
    'Phòng đơn cho sinh viên',
    'Phòng nhỏ gọn, phù hợp cho 1 người. Có giường, tủ quần áo, bàn học. Điện nước giá sinh hoạt.',
    1500000,
    '321 Tây Sơn, Đống Đa, Hà Nội',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    80000,
    30000,
    3000,
    @user2_id,
    @cat1_id,
    @loc1_id,
    NOW(),
    NOW()
),

-- Phòng của user 3
(
    'Phòng VIP có gác lửng',
    'Phòng 2 tầng, gác lửng rộng rãi. Có điều hòa 2 chiều, tủ lạnh, máy nước nóng năng lượng mặt trời.',
    3500000,
    '555 Khương Đình, Thanh Xuân, Hà Nội',
    'CHECKED_OUT',
    'ENABLE',
    1,
    0,
    120000,
    50000,
    3500,
    @user3_id,
    @cat2_id,
    @loc1_id,
    NOW(),
    NOW()
),
(
    'Căn hộ mini 2 phòng ngủ',
    'Căn hộ rộng rãi, 2 phòng ngủ, 1 phòng khách, bếp riêng. Phù hợp gia đình nhỏ hoặc nhóm bạn.',
    5500000,
    '888 Lê Duẩn, Hai Bà Trưng, Hà Nội',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    150000,
    80000,
    3500,
    @user3_id,
    @cat2_id,
    @loc1_id,
    NOW(),
    NOW()
),

-- Phòng của user 4
(
    'Homestay ấm cúng tại Hồ Tây',
    'Homestay phong cách Nhật Bản, view hồ Tây tuyệt đẹp. Không gian yên tĩnh, thích hợp nghỉ dưỡng.',
    6000000,
    '999 Võ Chí Công, Tây Hồ, Hà Nội',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    150000,
    70000,
    3500,
    @user4_id,
    @cat2_id,
    @loc1_id,
    NOW(),
    NOW()
),
(
    'Phòng trọ giá sinh viên',
    'Phòng rộng 18m2, có ban công phơi đồ. Điện nước giá dân, không chung chủ. Gần chợ, siêu thị.',
    1800000,
    '111 Xã Đàn, Đống Đa, Hà Nội',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    100000,
    40000,
    3500,
    @user4_id,
    @cat1_id,
    @loc1_id,
    NOW(),
    NOW()
),

-- Phòng của user 5
(
    'Phòng trọ Quận 1 giá tốt',
    'Vị trí trung tâm Q1, gần Bến Thành, Phố đi bộ Nguyễn Huệ. Phòng sạch đẹp, an ninh 24/7.',
    4000000,
    '222 Lê Lợi, Quận 1, TP.HCM',
    'ROOM_RENT',
    'ENABLE',
    1,
    0,
    120000,
    50000,
    3500,
    @user5_id,
    @cat2_id,
    @loc2_id,
    NOW(),
    NOW()
),
(
    'Căn hộ view sông Hàn Đà Nẵng',
    'Căn hộ cao cấp view sông Hàn, đầy đủ tiện nghi 5 sao. Gần biển Mỹ Khê, trung tâm thành phố.',
    7000000,
    '333 Trần Hưng Đạo, Đà Nẵng',
    'HIRED',
    'ENABLE',
    1,
    0,
    200000,
    100000,
    4000,
    @user5_id,
    @cat2_id,
    @loc1_id,
    NOW(),
    NOW()
);

-- Verify rooms
SELECT '=== FAKE ROOMS CREATED ===' as info;
SELECT 
    r.id,
    r.title,
    r.price,
    r.status,
    u.name as owner_name
FROM room r
JOIN users u ON r.user_id = u.id
ORDER BY r.created_at DESC
LIMIT 10;

-- ============================================
-- Bước 4: Insert TESTIMONIALS / REVIEWS
-- (Nếu có bảng review/testimonial)
-- ============================================

-- Kiểm tra xem có bảng nào không
-- SHOW TABLES LIKE '%review%';
-- SHOW TABLES LIKE '%testimonial%';
-- SHOW TABLES LIKE '%comment%';

-- Nếu có bảng review:
-- INSERT INTO review (user_id, room_id, rating, comment, created_at) VALUES
-- (@customer1_id, (SELECT id FROM room LIMIT 1), 5, 'Phòng rất đẹp, chủ nhà thân thiện!', NOW());

-- Nếu có bảng testimonial:
-- INSERT INTO testimonial (user_id, content, rating, created_at) VALUES
-- (@customer1_id, 'Dịch vụ tuyệt vời, tôi rất hài lòng!', 5, NOW());

-- ============================================
-- FAKE TESTIMONIALS DATA (để hiển thị trang chủ)
-- Lưu trực tiếp vào users hoặc tạo bảng riêng
-- ============================================

-- Option: Thêm cột testimonial vào users (nếu chưa có)
-- ALTER TABLE users ADD COLUMN testimonial TEXT;
-- UPDATE users SET testimonial = 'Các phòng trọ rất tuyệt vời sạch sẽ thoáng mát' WHERE id = @customer1_id;

SELECT '=== TESTIMONIALS USERS ===' as info;
SELECT 
    id,
    name,
    image_url,
    address as city,
    'Các phòng trọ rất tuyệt vời, sạch sẽ thoáng mát. Chủ nhà thân thiện, nhiệt tình hỗ trợ.' as testimonial
FROM users 
WHERE email = 'nguyen.minh.f@gmail.com'
UNION ALL
SELECT 
    id,
    name,
    image_url,
    address,
    'Không có lời nào diễn tả được cảm xúc của tôi lúc này. Dịch vụ quá xuất sắc!' as testimonial
FROM users 
WHERE email = 'tran.thu.g@gmail.com'
UNION ALL
SELECT 
    id,
    name,
    image_url,
    address,
    'Tôi đã tìm được phòng trọ ưng ý nhờ trang web này. Rất tiện lợi và nhanh chóng.' as testimonial
FROM users 
WHERE email = 'le.quang.h@gmail.com'
UNION ALL
SELECT 
    id,
    name,
    image_url,
    address,
    'Giá cả phải chăng, phòng trọ chất lượng. Tôi rất hài lòng với dịch vụ!' as testimonial
FROM users 
WHERE email = 'pham.lan.i@gmail.com'
UNION ALL
SELECT 
    id,
    name,
    image_url,
    address,
    'Website dễ sử dụng, thông tin rõ ràng. Đã giới thiệu cho bạn bè rất nhiều.' as testimonial
FROM users 
WHERE email = 'hoang.anh.k@gmail.com';

-- ============================================
-- Summary
-- ============================================
SELECT 
    (SELECT COUNT(*) FROM users) as total_users,
    (SELECT COUNT(*) FROM room) as total_rooms,
    (SELECT COUNT(*) FROM room WHERE status = 'ROOM_RENT') as available_rooms,
    (SELECT COUNT(*) FROM room WHERE status = 'HIRED') as hired_rooms;

-- ============================================
-- Kết quả:
-- - 10 phòng fake với địa chỉ thật
-- - 5 users có testimonials (đánh giá)
-- - Avatars + testimonials sẵn sàng hiển thị
-- ============================================

