CREATE TABLE IF NOT EXISTS tbl_user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tbl_listing (
    id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL UNIQUE,
    title VARCHAR(255),
    district VARCHAR(255),
    area NUMERIC,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tbl_price_snapshot (
    id BIGSERIAL PRIMARY KEY,
    listing_id BIGINT NOT NULL REFERENCES tbl_listing(id) ON DELETE CASCADE,
    price NUMERIC NOT NULL,
    snapshot_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_snapshot_listing
ON tbl_price_snapshot(listing_id, snapshot_at);

CREATE TABLE IF NOT EXISTS tbl_alert_subscription (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES tbl_user(id) ON DELETE CASCADE,
    district VARCHAR(100),
    price_drop_pct NUMERIC CHECK (price_drop_pct > 0),
    last_notified TIMESTAMP,
    create_at TIMESTAMP NOT NULL DEFAULT now()
);

-- tbl_user for user's registration, user's authentication
-- tbl_listing for room posts
-- tbl_price_snapshot for price history
-- tbl_alert_subscription for alert