CREATE TABLE roles (
  id SERIAL PRIMARY KEY,
  role_id INT UNIQUE NOT NULL,
  name VARCHAR(50) UNIQUE NOT NULL,
  description TEXT
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  user_name VARCHAR(100) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE,
  role_id INT NOT NULL DEFAULT 2,
  FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT
);