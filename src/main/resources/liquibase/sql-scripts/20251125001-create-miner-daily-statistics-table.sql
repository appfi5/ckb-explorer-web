CREATE TABLE IF NOT EXISTS miner_daily_statistics
(

    id                       BIGSERIAL PRIMARY KEY,

    created_at_unixtimestamp BIGINT         NULL     DEFAULT NULL,

    max_block_number         BIGINT         NULL     DEFAULT NULL,

    min_block_number         BIGINT         NULL     DEFAULT NULL,

    total_reward             NUMERIC(30, 0) NULL     DEFAULT NULL,

    total_hash_rate          VARCHAR(255)   NULL     DEFAULT NULL,

    avg_ror                  VARCHAR(255)   NULL     DEFAULT NULL,

    miners                   JSONB          NULL     DEFAULT NULL,

    created_at               TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at               TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    version                  INTEGER        NOT NULL DEFAULT 0
    );


CREATE UNIQUE INDEX IF NOT EXISTS idx_miner_daily_statistics_created_at_unixtimestamp ON miner_daily_statistics (created_at_unixtimestamp);