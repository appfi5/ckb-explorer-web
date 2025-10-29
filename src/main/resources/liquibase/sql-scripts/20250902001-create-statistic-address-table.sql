
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
    updated_at                    TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    transaction_fee_rates         jsonb            DEFAULT '{}'::jsonb,

    last_n_days_transaction_fee_rates varchar(2000)
    );

-- DailyStatistics 表创建SQL语句 - PostgreSQL版本
CREATE TABLE IF NOT EXISTS daily_statistics
(
    id                                     BIGSERIAL PRIMARY KEY,

    -- 基础统计字段
    transactions_count                     BIGINT         NULL     DEFAULT NULL,
    addresses_count                        BIGINT         NULL     DEFAULT NULL,
    total_dao_deposit                      VARCHAR(255)   NULL     DEFAULT NULL,
    block_timestamp                        BIGINT         NULL     DEFAULT NULL,
    max_block_number                       BIGINT         NULL     DEFAULT NULL,
    created_at_unixtimestamp               BIGINT         NULL     DEFAULT NULL,
    created_at                             TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                             TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                                INTEGER        NOT NULL DEFAULT 0,

    -- DAO相关字段
    dao_depositors_count                   VARCHAR(255)   NULL     DEFAULT NULL,
    unclaimed_compensation                 VARCHAR(255)   NULL     DEFAULT NULL,
    claimed_compensation                   VARCHAR(255)   NULL     DEFAULT NULL,
    average_deposit_time                   VARCHAR(255)   NULL     DEFAULT NULL,
    estimated_apc                          VARCHAR(255)   NULL     DEFAULT NULL,
    mining_reward                          VARCHAR(255)   NULL     DEFAULT NULL,
    deposit_compensation                   VARCHAR(255)   NULL     DEFAULT NULL,
    treasury_amount                        VARCHAR(255)   NULL     DEFAULT NULL,

    -- 区块和细胞相关字段
    live_cells_count                       VARCHAR(255)   NULL     DEFAULT NULL,
    dead_cells_count                       VARCHAR(255)   NULL     DEFAULT NULL,
    avg_hash_rate                          VARCHAR(255)   NULL     DEFAULT NULL,
    avg_difficulty                         VARCHAR(255)   NULL     DEFAULT NULL,
    uncle_rate                             VARCHAR(255)   NULL     DEFAULT NULL,
    total_depositors_count                 VARCHAR(255)   NULL     DEFAULT NULL,

    -- 交易费用和容量相关
    total_tx_fee                           NUMERIC(30, 0) NULL     DEFAULT NULL,
    occupied_capacity                      NUMERIC(30, 0) NULL     DEFAULT NULL,
    daily_dao_deposit                      NUMERIC(30, 0) NULL     DEFAULT NULL,
    daily_dao_depositors_count             INTEGER        NULL     DEFAULT NULL,
    daily_dao_withdraw                     NUMERIC(30, 0) NULL     DEFAULT NULL,
    circulation_ratio                      NUMERIC        NULL     DEFAULT NULL,
    total_supply                           NUMERIC(30, 0) NULL     DEFAULT NULL,
    circulating_supply                     NUMERIC        NULL     DEFAULT NULL,
    locked_capacity                        NUMERIC(30, 0) NULL     DEFAULT NULL,

    -- 分布和统计字段(JSON格式)
    address_balance_distribution           JSONB          NULL     DEFAULT NULL,
    block_time_distribution                JSONB          NULL     DEFAULT NULL,
    epoch_time_distribution                JSONB          NULL     DEFAULT NULL,
    average_block_time                     JSONB          NULL     DEFAULT NULL,
    nodes_distribution                     JSONB          NULL     DEFAULT NULL,
    ckb_hodl_wave                          JSONB          NULL     DEFAULT NULL,
    activity_address_contract_distribution JSONB          NULL     DEFAULT NULL,

    -- 其他统计字段
    nodes_count                            INTEGER        NULL     DEFAULT NULL,
    holder_count                           BIGINT         NULL     DEFAULT NULL,
    knowledge_size                         NUMERIC(30, 0) NULL     DEFAULT NULL
    );

-- 创建索引以提高查询性能
CREATE UNIQUE INDEX IF NOT EXISTS idx_daily_statistics_created_at_unixtimestamp ON daily_statistics (created_at_unixtimestamp);

CREATE TABLE IF NOT EXISTS epoch_statistics
(
    -- 主键ID，自增
    id                   BIGSERIAL PRIMARY KEY,
    -- Epoch编号（区块链中的纪元编号，唯一且非空）
    epoch_number         BIGINT NOT NULL UNIQUE,
    difficulty           character varying,
    uncle_rate           character varying,
    hash_rate            character varying,
    epoch_time           BIGINT,
    epoch_length         INTEGER,
    largest_tx_hash      bytea,
    largest_tx_bytes     BIGINT,
    max_tx_cycles        BIGINT,
    max_block_cycles     BIGINT,
    largest_block_number BIGINT,
    largest_block_size   BIGINT,
    created_at           TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_epoch_statistics_number ON epoch_statistics (epoch_number);

CREATE TABLE IF NOT EXISTS dao_contracts (
                                      id BIGSERIAL PRIMARY KEY,
                                      total_deposit numeric(30,0) DEFAULT 0.0,
                                      depositors_count integer DEFAULT 0,
                                      claimed_compensation numeric(30,0) DEFAULT 0.0,
                                      unclaimed_compensation numeric(30,0),
                                      created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS udt_daily_statistics (
                                              id BIGSERIAL PRIMARY KEY,
                                              script_id bigint NOT NULL,
                                              ckb_transactions_count integer DEFAULT 0,
                                              holders_count integer DEFAULT 0,
                                              created_at_unixtimestamp integer,
                                              version integer NOT NULL DEFAULT 0,
                                              created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS index_on_udt_id_and_unixtimestamp ON public.udt_daily_statistics USING btree (script_id, created_at_unixtimestamp);