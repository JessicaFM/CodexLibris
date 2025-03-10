INSERT INTO authors (author_id, name, birth_date, nationality) VALUES
(1, 'J.K. Rowling', '1965-07-31', 'Regne Unit'),
(2, 'George Orwell', '1903-06-25', 'Regne Unit'),
(3, 'Mercè Rodoreda', '1908-10-10', 'Catalunya'),
(4, 'Albert Sánchez Piñol', '1965-07-11', 'Catalunya'),
(5, 'Jane Austen', '1775-12-16', 'Regne Unit');

INSERT INTO genres (genre_id, name, description) VALUES
(1, 'Fantasía', 'Històries ambientades en mons màgics o amb elements sobrenaturals.'),
(2, 'Ciència-ficció', 'Narracions basades en avenços científics i tecnològics.'),
(3, 'Realisme màgic', 'Històries amb elements màgics dins un context realista.'),
(4, 'Ficció contemporània', 'Novel·les modernes centrades en la societat actual.'),
(5, 'Romàntic', 'Històries centrades en relacions amoroses.');

INSERT INTO books (title, author_id, isbn, published_date, genre_id, available) VALUES
('Harry Potter i la pedra filosofal', 1, '978-8476290921', '1997-06-26', 1, TRUE),
('1984', 2, '978-8499890944', '1949-06-08', 2, TRUE),
('La plaça del diamant', 3, '978-8429753439', '1962-01-01', 4, TRUE),
('La pell freda', 4, '978-8475882806', '2002-09-12', 3, TRUE),
('Orgull i prejudici', 5, '978-8491050629', '1813-01-28', 5, TRUE);

