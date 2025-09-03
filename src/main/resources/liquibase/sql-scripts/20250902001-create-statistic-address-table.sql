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
