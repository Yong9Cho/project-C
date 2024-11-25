-- 회원 등급 테이블
DROP TABLE IF EXISTS user_grades;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS withdrawals;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS chip_transactions;
DROP TABLE IF EXISTS betting_records;
DROP TABLE IF EXISTS inquiries;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS settlements;

CREATE TABLE IF NOT EXISTS user_grades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    benefits TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    registration_status VARCHAR(20) NOT NULL,
    approved_by BIGINT,
    grade_id BIGINT,
    parent_id BIGINT,
    current_balance DECIMAL(20,2) DEFAULT 0,
    total_deposit DECIMAL(20,2) DEFAULT 0,
    total_withdrawal DECIMAL(20,2) DEFAULT 0,
    total_betting DECIMAL(20,2) DEFAULT 0,
    total_winning DECIMAL(20,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    last_login_ip VARCHAR(45),
    memo TEXT,
    FOREIGN KEY (parent_id) REFERENCES users(id),
    FOREIGN KEY (grade_id) REFERENCES user_grades(id),
    FOREIGN KEY (approved_by) REFERENCES users(id)
);

-- 충전 요청 테이블
CREATE TABLE IF NOT EXISTS deposits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    approved_by BIGINT,
    request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_time TIMESTAMP,
    memo TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id)
);

-- 환전 요청 테이블
CREATE TABLE IF NOT EXISTS withdrawals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    approved_by BIGINT,
    request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_time TIMESTAMP,
    memo TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id)
);

-- 게임 테이블
CREATE TABLE IF NOT EXISTS games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 알(칩) 거래 내역 테이블
CREATE TABLE IF NOT EXISTS chip_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    game_id BIGINT,
    amount DECIMAL(20,2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    balance_after DECIMAL(20,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- 배팅 기록 테이블
CREATE TABLE IF NOT EXISTS betting_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    bet_amount DECIMAL(20,2) NOT NULL,
    win_amount DECIMAL(20,2),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- 1:1 문의 테이블
CREATE TABLE IF NOT EXISTS inquiries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 쪽지 테이블
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    read_status BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);

-- 정산 내역 테이블
CREATE TABLE IF NOT EXISTS settlements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    settlement_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    period_start TIMESTAMP NOT NULL,
    period_end TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
