CREATE SEQUENCE assets_id_seq;

CREATE TABLE assets
(
    ID           INTEGER      NOT NULL DEFAULT nextval('assets_id_seq'),
    filename     VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    url          VARCHAR(1000),
    size         BIGINT,
    upload_date  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    content      BYTEA
);

ALTER SEQUENCE assets_id_seq OWNED BY assets.id;

-- Add any indexes you might need
CREATE INDEX IF NOT EXISTS idx_assets_filename ON assets (filename);
CREATE INDEX IF NOT EXISTS idx_assets_upload_date ON assets (upload_date);
CREATE INDEX IF NOT EXISTS idx_assets_content_type ON assets (content_type);