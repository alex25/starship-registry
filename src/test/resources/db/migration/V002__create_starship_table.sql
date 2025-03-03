-- 002_create_starship_table.sql
CREATE TABLE starship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    movie_id BIGINT,
    CONSTRAINT fk_movie
        FOREIGN KEY (movie_id) 
        REFERENCES movie(id)
        ON DELETE SET NULL
);
