CREATE TABLE movie (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    release_year INT,
    genre TEXT,
    rating DOUBLE PRECISION,
    director TEXT NOT NULL,
    duration INT
);

CREATE TABLE tv_show (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    release_year INT,
    genre TEXT,
    rating DOUBLE PRECISION,
    episode_count INT,
    season_count INT
);

CREATE TABLE season_rating (
    show_id UUID NOT NULL,
    season_num INT NOT NULL,
    rating DOUBLE PRECISION,

    PRIMARY KEY (show_id, season_num),
    FOREIGN KEY (show_id)
        REFERENCES tv_show(id)
        ON DELETE CASCADE
);