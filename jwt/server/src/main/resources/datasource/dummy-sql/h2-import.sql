-- 가입자 dummy data
INSERT INTO member.users (
    user_id, username, password, email, phone, birth, address, zipcode,
    activated, role, created_at, updated_at, deleted_at, last_login_at, email_verified
) VALUES
    ('jumong', '고주몽', 'jumong', 'jumong@example.com', '010-9876-9765', DATE '1985-07-10',
     '제주도 제주시 서광로 3길 41번지', '63175', TRUE, 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE),

    ('cuti7', '김기업', 'cui7', 'cuti7@example.com', '010-2536-6574', DATE '1990-10-21',
     '서울 강남구 도산대로17길 10 B1 새들러하우스', '06037', TRUE, 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, FALSE);
