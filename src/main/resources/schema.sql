DROP TABLE IF EXISTS media_items;

CREATE TABLE media_items (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    release_year INT,
    genre TEXT,
    rating DOUBLE PRECISION,
    media_type TEXT NOT NULL,
    episode_count INT
);