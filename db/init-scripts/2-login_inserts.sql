INSERT INTO roles (id, name, description) VALUES
(1, 'admin', 'Administrator with full access'),
(2, 'user', 'Regular user with limited access');

INSERT INTO users (user_name, first_name, last_name, email, password, is_active, role_id) VALUES
('admin', 'Admin Name', 'Admin Last Name', 'admin@example.com', '$2b$10$9Y8mgvz0/SoltKts.JI0s.6W8QbvrMN/kZiYVbj0frTG3eT/H3Gre', TRUE, 1),
('geral_rivia', 'Geral', 'Rivia', 'geral_rivia@example.com', '$2a$10$gOjEF21WvvUDg04fH9LemeBiBWJxJdXFsg2vYCOMw4uDsbZ1/fH9i', TRUE, 2),
('laia_miret', 'Laia', 'Miret', 'laia.miret@example.com', '$2a$10$gOjEF21WvvUDg04fH9LemeBiBWJxJdXFsg2vYCOMw4uDsbZ1/fH9i', TRUE, 2),
('oriol_sendra', 'Oriol', 'Sendra', 'oriol.sendra@example.com', '$2a$10$gOjEF21WvvUDg04fH9LemeBiBWJxJdXFsg2vYCOMw4uDsbZ1/fH9i', TRUE, 2);
