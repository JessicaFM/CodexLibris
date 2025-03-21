INSERT INTO loan_status (name, description) VALUES
('Retornat', 'El llibre ha estat retornat i esta disponible per a un nou prestec.'),
('Pendent', 'El prestec ha estat sol·licitat, pero encara no ha estat aprovat.'),
('En préstec', 'El llibre ha estat prestat i esta en possessio de l''usuari.'),
('Retrasat', 'El prestec ha superat la data limit de devolucio.'),
('Cancel·lat', 'El prestec ha estat cancel·lat abans de l''aprovacio.'),
('Perdut', 'El llibre prestat no ha estat retornat i es considera perdut.');

INSERT INTO loan (user_id, book_id, loan_date, due_date, return_date, status_id) VALUES
(2, 5, '2025-04-16 10:00:00', '2025-05-16 10:00:00', NULL, 1),
(2, 12, '2025-03-10 15:30:00', '2025-06-10 15:30:00', NULL, 2),
(3, 21, '2025-02-20 09:00:00', '2025-07-20 09:00:00', '2025-03-18 18:45:00', 3);
