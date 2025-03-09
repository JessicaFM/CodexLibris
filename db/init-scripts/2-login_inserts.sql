INSERT INTO roles (role_id, name, description) VALUES
(1, 'admin', 'Administrator with full access'),
(2, 'user', 'Regular user with limited access');

INSERT INTO users (user_name, first_name, last_name, email, password, is_active, role_id) VALUES
('admin', 'Admin Name', 'Admin Last Name', 'admin@example.com', '$2b$10$9Y8mgvz0/SoltKts.JI0s.6W8QbvrMN/kZiYVbj0frTG3eT/H3Gre', TRUE, 1);
INSERT INTO users (user_name, first_name, last_name, email, password, is_active, role_id) VALUES
('geral_rivia', 'Geral', 'Rivia', 'geral_rivia@example.com', '$2b$10$1MHIBGq7/JBcyRKnn02z2ulabXYx1cK46h034pn.8stxLiqvuZ46y', TRUE, 2);