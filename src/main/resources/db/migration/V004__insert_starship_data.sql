-- 004_insert_starship_data.sql
INSERT INTO starship (name, movie_id) VALUES 
('Millennium Falcon', (SELECT id FROM movie WHERE title = 'Star Wars: A New Hope')),
('X-Wing', (SELECT id FROM movie WHERE title = 'Star Wars: A New Hope')),
('TIE Fighter', (SELECT id FROM movie WHERE title = 'The Empire Strikes Back')),
('Slave I', (SELECT id FROM movie WHERE title = 'The Empire Strikes Back')),
('Razor Crest', (SELECT id FROM movie WHERE title = 'The Mandalorian'));
