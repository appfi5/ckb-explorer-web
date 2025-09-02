-- 创建地址统计表：statistic_address
CREATE TABLE IF NOT EXISTS statistic_address (
    id BIGSERIAL PRIMARY KEY,
    lock_script_id BIGINT NOT NULL,
    script_hash BYTEA NOT NULL,
    balance BIGINT DEFAULT 0 NOT NULL,
    live_cells_count BIGINT DEFAULT 0 NOT NULL,
    balance_occupied BIGINT DEFAULT 0 NOT NULL,
    -- 添加创建时间和更新时间字段
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 为lock_script_id添加索引，提高查询效率
CREATE INDEX IF NOT EXISTS idx_statistic_address_lock_script_id ON statistic_address(lock_script_id);

-- 为script_hash添加索引，支持根据脚本哈希查询
CREATE INDEX IF NOT EXISTS idx_statistic_address_script_hash ON statistic_address(script_hash);
