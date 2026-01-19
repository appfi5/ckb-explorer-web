CREATE TABLE IF NOT EXISTS app_info
(

    id                       BIGSERIAL PRIMARY KEY,

    app_name                 VARCHAR(255)   NULL     DEFAULT NULL,

    api_key                  VARCHAR(255)   NULL     DEFAULT NULL,

    disabled                 INTEGER        NOT NULL DEFAULT 0,

    created_at               TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at               TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE UNIQUE INDEX IF NOT EXISTS idx_app_info_api_key ON app_info (api_key, disabled);