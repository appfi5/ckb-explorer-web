-- 创建地址统计表：statistic_address
CREATE TABLE IF NOT EXISTS statistic_address
(
    id               BIGSERIAL PRIMARY KEY,
    lock_script_id   BIGINT           NOT NULL,
    script_hash      BYTEA            NOT NULL,
    balance          BIGINT DEFAULT 0 NOT NULL,
    live_cells_count BIGINT DEFAULT 0 NOT NULL,
    balance_occupied BIGINT DEFAULT 0 NOT NULL,
    -- 添加创建时间和更新时间字段
    created_at       TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 为lock_script_id添加索引，提高查询效率
CREATE INDEX IF NOT EXISTS idx_statistic_address_lock_script_id ON statistic_address (lock_script_id);

-- 为script_hash添加索引，支持根据脚本哈希查询
CREATE INDEX IF NOT EXISTS idx_statistic_address_script_hash ON statistic_address (script_hash);

CREATE INDEX IF NOT EXISTS idx_stat_addr_balance_id ON statistic_address (balance DESC, lock_script_id);


CREATE TABLE IF NOT EXISTS statistic_infos
(
    -- 主键ID，自增
    id                            BIGSERIAL PRIMARY KEY,

    -- 过去24小时的交易数量
    transactions_last_24hrs       BIGINT           NOT NULL DEFAULT 0,

    -- 每分钟交易数量
    transactions_count_per_minute BIGINT           NOT NULL DEFAULT 0,

    -- 平均区块时间（秒）
    average_block_time            DOUBLE PRECISION NOT NULL DEFAULT 0.0,

    -- 哈希率（算力）
    hash_rate                     NUMERIC(30, 6)   NOT NULL DEFAULT 0.0,

    -- 区块链基本信息
    blockchain_info               CHARACTER VARYING(500)    DEFAULT NULL,

    -- 地址余额排名（JSON格式）
    address_balance_ranking       JSONB                     DEFAULT NULL,

    -- 系统字段：创建时间
    created_at                    TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 系统字段：更新时间
    updated_at                    TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- DailyStatistics 表创建SQL语句 - PostgreSQL版本
CREATE TABLE IF NOT EXISTS daily_statistics
(
    id                                     BIGSERIAL PRIMARY KEY,

    -- 基础统计字段
    transactions_count                     BIGINT       NULL     DEFAULT NULL,
    addresses_count                        BIGINT       NULL     DEFAULT NULL,
    total_dao_deposit                      VARCHAR(255) NULL     DEFAULT NULL,
    block_timestamp                        BIGINT       NULL     DEFAULT NULL,
    created_at_unixtimestamp               BIGINT       NULL     DEFAULT NULL,
    created_at                             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- DAO相关字段
    dao_depositors_count                   VARCHAR(255) NULL     DEFAULT NULL,
    unclaimed_compensation                 VARCHAR(255) NULL     DEFAULT NULL,
    claimed_compensation                   VARCHAR(255) NULL     DEFAULT NULL,
    average_deposit_time                   VARCHAR(255) NULL     DEFAULT NULL,
    estimated_apc                          VARCHAR(255) NULL     DEFAULT NULL,
    mining_reward                          VARCHAR(255) NULL     DEFAULT NULL,
    deposit_compensation                   VARCHAR(255) NULL     DEFAULT NULL,
    treasury_amount                        VARCHAR(255) NULL     DEFAULT NULL,

    -- 区块和细胞相关字段
    live_cells_count                       VARCHAR(255) NULL     DEFAULT NULL,
    dead_cells_count                       VARCHAR(255) NULL     DEFAULT NULL,
    avg_hash_rate                          VARCHAR(255) NULL     DEFAULT NULL,
    avg_difficulty                         VARCHAR(255) NULL     DEFAULT NULL,
    uncle_rate                             VARCHAR(255) NULL     DEFAULT NULL,
    total_depositors_count                 VARCHAR(255) NULL     DEFAULT NULL,

    -- 交易费用和容量相关
    total_tx_fee                           NUMERIC(40)  NULL     DEFAULT NULL,
    occupied_capacity                      NUMERIC(40)  NULL     DEFAULT NULL,
    daily_dao_deposit                      NUMERIC(40)  NULL     DEFAULT NULL,
    daily_dao_depositors_count             INTEGER      NULL     DEFAULT NULL,
    daily_dao_withdraw                     NUMERIC(40)  NULL     DEFAULT NULL,
    circulation_ratio                      NUMERIC(40)  NULL     DEFAULT NULL,
    total_supply                           NUMERIC(40)  NULL     DEFAULT NULL,
    circulating_supply                     NUMERIC(40)  NULL     DEFAULT NULL,
    locked_capacity                        NUMERIC(40)  NULL     DEFAULT NULL,

    -- 分布和统计字段(JSON格式)
    address_balance_distribution           JSONB        NULL     DEFAULT NULL,
    block_time_distribution                JSONB        NULL     DEFAULT NULL,
    epoch_time_distribution                JSONB        NULL     DEFAULT NULL,
    epoch_length_distribution              JSONB        NULL     DEFAULT NULL,
    average_block_time                     JSONB        NULL     DEFAULT NULL,
    nodes_distribution                     JSONB        NULL     DEFAULT NULL,
    ckb_hodl_wave                          JSONB        NULL     DEFAULT NULL,
    activity_address_contract_distribution JSONB        NULL     DEFAULT NULL,

    -- 其他统计字段
    nodes_count                            INTEGER      NULL     DEFAULT NULL,
    holder_count                           BIGINT       NULL     DEFAULT NULL,
    knowledge_size                         BIGINT       NULL     DEFAULT NULL
    );

-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_daily_statistics_created_at_unixtimestamp ON daily_statistics (created_at_unixtimestamp);
