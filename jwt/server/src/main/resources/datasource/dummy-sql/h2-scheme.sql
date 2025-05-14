CREATE SCHEMA IF NOT EXISTS member;
CREATE SCHEMA IF NOT EXISTS board;
SET REFERENTIAL_INTEGRITY TRUE;

-- users 테이블 생성 (기본 사용자 정보)
CREATE TABLE IF NOT EXISTS member.users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,  -- 사용자 이름
    email VARCHAR(150) NOT NULL UNIQUE,  -- 이메일
    role VARCHAR(20) NOT NULL,  -- 사용자 권한 (ex: ADMIN, USER)
    activated BOOLEAN NOT NULL DEFAULT TRUE,  -- 계정 활성화 여부
    deleted_at TIMESTAMP NULL,  -- 삭제 시간 (소프트 삭제 처리)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 수정 시간
    email_verified BOOLEAN DEFAULT FALSE  -- 이메일 인증 여부
--    INDEX idx_user_email (email),  -- 이메일 인덱스
--    INDEX idx_user_phone (phone)  -- 전화번호 인덱스
);
-- 인덱스 구문을 분리하여 별도로 작성
CREATE INDEX idx_user_email ON member.users (email);

-- social_logins 테이블 생성
CREATE TABLE IF NOT EXISTS member.social_logins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,   -- 소셜 로그인 고유 ID
    user_id BIGINT NOT NULL,                -- 사용자 ID (users 테이블의 외래키)
    provider VARCHAR(255) NOT NULL,         -- 소셜 로그인 제공자 (Google, Kakao 등)
    provider_user_id VARCHAR(255) NOT NULL, -- 소셜 로그인 제공자에서 제공하는 고유 ID
    nickname VARCHAR(255),                  -- 사용자 nickname
    email VARCHAR(255),              -- 소셜 로그인에 대한 액세스 토큰 (선택적)
    activated BOOLEAN NOT NULL DEFAULT TRUE, -- 활성화 여부
    deleted_at TIMESTAMP,                    -- 삭제 시간 (NULL이면 삭제되지 않음)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES member.users(id) ON DELETE CASCADE  -- users 테이블과의 외래키 관계
);

-- user_credentials 테이블 생성 (비밀번호와 관련된 정보)
CREATE TABLE IF NOT EXISTS member.user_credentials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,  -- 사용자 ID (users 테이블의 외래키)
    password VARCHAR(255) NOT NULL,  -- 해시된 비밀번호
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 수정 시간
    FOREIGN KEY (user_id) REFERENCES member.users(id) ON DELETE CASCADE  -- users 테이블과의 외래키 관계
--    INDEX idx_user_id (user_id)  -- user_id에 대한 인덱스
);

-- user_credentials 테이블에 대한 인덱스 생성
CREATE INDEX idx_user_credentials_user_id ON member.user_credentials (user_id);

-- user 기타 정보 테이블 생성
CREATE TABLE IF NOT EXISTS member.user_profiles (
    user_id BIGINT PRIMARY KEY,           -- users.id 참조
    name VARCHAR(50),                     -- 실명
    phone VARCHAR(20) UNIQUE,             -- 전화번호
    birth DATE,                           -- 생년월일
    gender VARCHAR(10),                   -- 성별
    activated BOOLEAN NOT NULL DEFAULT TRUE,  -- 계정 활성화 여부
    deleted_at TIMESTAMP NULL,  -- 삭제 시간 (소프트 삭제 처리)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- user_password_history 테이블 생성 (비밀번호 변경 이력)
CREATE TABLE IF NOT EXISTS member.user_password_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,  -- 사용자 ID (users 테이블의 외래키)
    password VARCHAR(255) NOT NULL,  -- 이전 비밀번호 (해시된 비밀번호)
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 비밀번호 변경 일시
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 수정 시간
    FOREIGN KEY (user_id) REFERENCES member.users(id) ON DELETE CASCADE  -- users 테이블과의 외래키 관계
--    INDEX idx_user_password_history_user_id (user_id),  -- user_id에 대한 인덱스
--    INDEX idx_user_password_history_changed_at (changed_at)  -- 비밀번호 변경 일시에 대한 인덱스
);

-- user_password_history 테이블에 대한 인덱스 생성
CREATE INDEX idx_user_password_history_user_id ON member.user_password_history (user_id);
CREATE INDEX idx_user_password_history_changed_at ON member.user_password_history (changed_at);

-- jwt_token 테이블 생성 (Refresh Token 관련 정보)
CREATE TABLE IF NOT EXISTS member.jwt_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,  -- 사용자 ID (users 테이블의 외래키)
    user_agent VARCHAR(255),  -- 기기별 ID
    secret_key VARCHAR(255),  -- UUID
    client_ip VARCHAR(255),  -- 접속 IP
    activated BOOLEAN DEFAULT TRUE,  -- Refresh Token의 활성화 상태 (기본값은 TRUE)
    access_token VARCHAR(512) NOT NULL,  -- Access Token
    refresh_token VARCHAR(512) NOT NULL,  -- Refresh Token
    refresh_token_created_at TIMESTAMP NOT NULL,  -- Refresh Token 생성 시간
    refresh_token_updated_at TIMESTAMP NULL,  -- Refresh Token 갱신 시간
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 수정 시간
    FOREIGN KEY (user_id) REFERENCES member.users(id) ON DELETE CASCADE  -- users 테이블과의 외래키 관계
--    INDEX idx_refresh_token (refresh_token)  -- refresh_token에 대한 인덱스
);

-- jwt_token 테이블에 대한 인덱스 생성
CREATE INDEX idx_refresh_token ON member.jwt_token (refresh_token);

-- login_history 테이블 생성 (로그인 관련 정보)
CREATE TABLE IF NOT EXISTS member.login_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,  -- 사용자 ID (users 테이블의 외래키)
    logout_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 로그아웃 시간
    login_ip VARCHAR(45),  -- 로그인 IP
    user_agent VARCHAR(255),  -- User-Agent (로그인한 디바이스 정보 등)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 수정 시간
    FOREIGN KEY (user_id) REFERENCES member.users(id) ON DELETE CASCADE  -- users 테이블과의 외래키 관계
--    INDEX idx_user_login (user_id)  -- user_id에 대한 인덱스
);

-- login_history 테이블에 대한 인덱스 생성
CREATE INDEX idx_user_login_user_id ON member.login_history (user_id);

-- 이메일 인증 관련 테이블 생성
CREATE TABLE IF NOT EXISTS member.email_verification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 인증 기록 ID
    token VARCHAR(255) NOT NULL,  -- 인증 토큰 (랜덤 문자열)
    email VARCHAR(255) NOT NULL,  -- 인증 email
    is_verified BOOLEAN DEFAULT FALSE,  -- 인증 여부 (기본값: 인증되지 않음)
    expiration_at TIMESTAMP NOT NULL,  -- 인증 유효기간
    email_sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 이메일 발송 시간
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 수정 시간
);

-- email_verification 테이블에 대한 인덱스 생성
CREATE INDEX idx_email ON member.email_verification (email);
CREATE INDEX idx_email_token ON member.email_verification (token);

-- user_address 테이블 생성 (주소 전용)
CREATE TABLE IF NOT EXISTS member.user_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,                 -- 사용자 ID (users 테이블의 외래키)
    seq INT NOT NULL DEFAULT 0,              -- 우선순위 (숫자가 낮을수록 높은 우선순위)
    road_address VARCHAR(255) NOT NULL,      -- 도로명 주소
    jibun_address VARCHAR(255),              -- 지번 주소 (nullable)
    detail_address VARCHAR(255),             -- 상세주소 (nullable)
    extra_address VARCHAR(255),              -- 참고항목 (nullable)
    postcode VARCHAR(20) NOT NULL,            -- 우편번호
    activated BOOLEAN NOT NULL DEFAULT TRUE, -- 활성화 여부
    deleted_at TIMESTAMP,                    -- 삭제 시간 (NULL이면 삭제되지 않음)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 수정 시간
    FOREIGN KEY (user_id) REFERENCES member.users(id) ON DELETE CASCADE
    -- INDEX (user_id),  -- 필요시 인덱스 추가
    -- INDEX (zipcode)   -- 필요시 인덱스 추가
);
-- user_address 테이블에 대한 인덱스 생성
CREATE INDEX idx_user_address_user_id ON member.user_address (user_id);
CREATE INDEX idx_user_address_postcode ON member.user_address (postcode);

-- user_address 테이블 생성 (주소 전용)
CREATE TABLE IF NOT EXISTS board.todo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,                -- 사용자 ID (users 테이블의 외래키)
    title VARCHAR(255) NOT NULL,            -- 제목 (최대 255자)
    category VARCHAR(255) NOT NULL,         -- 제목 (최대 255자)
    start_date DATETIME NOT NULL,           -- 시작 일시
    end_date DATETIME NOT NULL,             -- 종료 일시
    description VARCHAR(500),               -- 설명 (최대 500자, 선택적)
    all_day BOOLEAN DEFAULT FALSE,          -- 종일 이벤트 여부
    completed BOOLEAN DEFAULT FALSE,        -- Todo 완료 상태
    activated BOOLEAN NOT NULL DEFAULT TRUE, -- 활성화 여부
    deleted_at TIMESTAMP,                    -- 삭제 시간 (NULL이면 삭제되지 않음)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 수정 시간
    FOREIGN KEY (user_id) REFERENCES member.users(id) -- users 테이블과 연결
);
