CREATE TABLE IF NOT EXISTS rental_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    expired_at DATETIME,
    used_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE INDEX idx_rental_code_code ON rental_code(code);
CREATE INDEX idx_rental_code_user_id ON rental_code(user_id);

