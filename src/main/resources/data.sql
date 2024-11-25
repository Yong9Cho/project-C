-- 사용자 등급 초기 데이터
INSERT INTO user_grades (id, name, level, description, min_deposit, max_withdrawal_per_day, created_at, updated_at)
VALUES 
(1, 'BRONZE', 1, '신규 회원', 10000, 1000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'SILVER', 2, '일반 회원', 100000, 3000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'GOLD', 3, '우수 회원', 1000000, 5000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'VIP', 4, 'VIP 회원', 5000000, 10000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'VVIP', 5, 'VVIP 회원', 10000000, 50000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 관리자 계정 생성 (비밀번호: admin123)
INSERT INTO users (username, password, role, status, registration_status, grade_id, created_at, updated_at, current_balance, total_deposit, total_withdrawal, total_betting, total_winning)
VALUES 
('admin', 'admin123', 'ADMIN', 'ACTIVE', 'APPROVED', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0, 0, 0, 0);

-- 파트너 계정 생성 (비밀번호: partner123)
INSERT INTO users (username, password, role, status, registration_status, grade_id, created_at, updated_at, current_balance, total_deposit, total_withdrawal, total_betting, total_winning)
VALUES 
('partner', 'partner123', 'PARTNER', 'ACTIVE', 'APPROVED', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0, 0, 0, 0);

-- 테스트 사용자 계정 생성 (비밀번호: user123)
INSERT INTO users (username, password, role, status, registration_status, grade_id, created_at, updated_at, current_balance, total_deposit, total_withdrawal, total_betting, total_winning)
VALUES 
('user1', 'user123', 'USER', 'ACTIVE', 'APPROVED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0, 0, 0, 0),
('user2', 'user123', 'USER', 'ACTIVE', 'APPROVED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0, 0, 0, 0);

-- 기본 게임 생성
INSERT INTO games (name, description, type, status, created_by, min_bet, max_bet, total_betting, total_payout, total_revenue, created_at, updated_at)
VALUES 
('Baccarat', '바카라 게임', 'CARD', 'ACTIVE', 1, 10000, 1000000, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Blackjack', '블랙잭 게임', 'CARD', 'ACTIVE', 1, 10000, 1000000, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Roulette', '룰렛 게임', 'TABLE', 'ACTIVE', 1, 5000, 500000, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Slot Machine', '슬롯 머신', 'SLOT', 'ACTIVE', 1, 1000, 100000, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
