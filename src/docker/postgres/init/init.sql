-- Ensure the sequence exists before creating the table
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_sequences
                       WHERE schemaname = 'public' AND sequencename = 'assets_id_seq') THEN
            CREATE SEQUENCE assets_id_seq;
        END IF;
    END
$$;

-- Create table with IF NOT EXISTS
CREATE TABLE IF NOT EXISTS assets
(
    id           INTEGER      NOT NULL DEFAULT nextval('assets_id_seq'),
    filename     VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    url          VARCHAR(1000),
    size         BIGINT,
    upload_date  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    filetype     VARCHAR(50),
    PRIMARY KEY (id) -- Add primary key for better indexing
);

-- Associate the sequence with the table's ID column
ALTER SEQUENCE assets_id_seq OWNED BY assets.id;

-- Create indexes if they don't already exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_assets_filename') THEN
            CREATE INDEX idx_assets_filename ON assets (filename);
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_assets_upload_date') THEN
            CREATE INDEX idx_assets_upload_date ON assets (upload_date);
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_assets_content_type') THEN
            CREATE INDEX idx_assets_content_type ON assets (content_type);
        END IF;
    END
$$;
