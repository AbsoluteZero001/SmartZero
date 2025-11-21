-- Bank System Database Schema

-- User Information Table
CREATE TABLE user_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_type VARCHAR(10) NOT NULL COMMENT '卡类型：1储蓄卡，2信用卡，3社保卡',
    card_no VARCHAR(50) NOT NULL UNIQUE COMMENT '卡号',
    password VARCHAR(100) NOT NULL COMMENT '账户密码',
    nickname VARCHAR(50) COMMENT '用户名',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    address VARCHAR(200) COMMENT '住址',
    id_num VARCHAR(50) NOT NULL COMMENT '身份证号',
    mobile VARCHAR(20) NOT NULL COMMENT '手机号',
    state INT DEFAULT 1 COMMENT '卡状态：0禁用，1启用，2冻结',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除：1是，0否',
    create_by BIGINT COMMENT '创建者id, 1管理员',
    update_by BIGINT COMMENT '更新者id, 1管理员',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_mobile (mobile),
    INDEX idx_card_no (card_no)
);

-- Admin Information Table
CREATE TABLE admin_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '别名',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    mobile VARCHAR(20) NOT NULL COMMENT '手机号',
    state VARCHAR(10) DEFAULT '1' COMMENT '状态',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',
    create_by BIGINT COMMENT '创建时间',
    update_by BIGINT COMMENT '更新时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建人id',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新人id',
    INDEX idx_mobile (mobile)
);

-- User Amount/Balance Table
CREATE TABLE user_amount (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount BIGINT COMMENT '金额',
    balance BIGINT COMMENT '余额',
    status INT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除 true-是 false-否',
    create_by BIGINT COMMENT '创建者id',
    update_by BIGINT COMMENT '更新者id',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user_info(id),
    INDEX idx_user_id (user_id)
);

-- Transaction Records Table
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    transaction_type VARCHAR(20) NOT NULL COMMENT '交易类型: deposit, withdrawal, transfer_in, transfer_out',
    amount BIGINT NOT NULL COMMENT '交易金额（以分为单位）',
    balance_after BIGINT NOT NULL COMMENT '交易后余额（以分为单位）',
    description VARCHAR(200) COMMENT '交易描述',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除：1是，0否',
    create_by BIGINT COMMENT '创建者id',
    update_by BIGINT COMMENT '更新者id',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user_info(id),
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
);

-- System Log Table
CREATE TABLE system_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type INT COMMENT '类型，0登出，1登入',
    user_type INT COMMENT '用户类型，0管理员，1用户',
    user_id BIGINT COMMENT '用户id',
    state INT DEFAULT 1 COMMENT '状态, 0禁用，1正常',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除，1是，0否',
    create_by BIGINT COMMENT '创建者id, 1 - 管理员',
    update_by BIGINT COMMENT '更新者id, 1 - 管理员',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
);