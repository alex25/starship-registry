-- 001_create_movie_table.sql
CREATE TABLE movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_year INT,
    is_tv_series BOOLEAN
);
