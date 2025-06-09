
# 📚 CodexLibris – REST API for a Digital Library

CodexLibris is a **RESTful API** developed with **Spring Boot**, **PostgreSQL**, and **JWT authentication**, designed to manage a digital library of books, authors, genres, and users. The project models real-world library features like book availability, role-based access control, and personalized recommendations via a local **LLM (Large Language Model)**.

Everything runs in Docker containers, making the setup and deployment seamless.

---

## 🧩 Tech Stack

- **Java + Spring Boot** (API, security, business logic)
- **PostgreSQL** (relational data persistence)
- **JWT** (token-based authentication)
- **Docker + Docker Compose** (containerized infrastructure)
- **OpenAPI/Swagger** (interactive API docs)
- Optional: **Local LLM integration** for book recommendations (Python + Transformers)

---

## 🎯 Features

- User management with role-based access (`ADMIN`, `USER`)
- CRUD operations for:
  - Books
  - Authors
  - Genres
  - Loads
  - Events
  - Users
  - Roles
- JWT-based login and secure endpoints
- Swagger UI for API exploration
- HTTPS enabled with self-signed cert
- Bonus: AI-powered recommendations with local LLM

---

## 🚀 Getting Started

### 🔧 1. Build Docker Containers
```sh
docker-compose build --no-cache
```

### ▶️ 2. Run the App
```sh
docker-compose up -d
```

### ✅ 3. Check Running Containers
```sh
docker ps
```

---

## 🔐 HTTPS Notes

- Access the API at: `https://localhost`
- Because of the self-signed certificate:
  - Use `-k` with `curl` to bypass SSL verification
  - In the browser, manually accept the certificate

---

## 🧪 API Documentation (Swagger)

[https://localhost/swagger-ui/index.html](https://localhost/swagger-ui/index.html)

---

## 🔐 Authentication

Use the `/auth/login` endpoint to obtain a JWT token.

```sh
curl -X POST https://localhost/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}'
```

Example response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "roleId": 1
}
```

---

## 📚 API Endpoints

### Users
```sh
GET /user/{id}
GET /users
```

### Books
```sh
GET /books
GET /books/{id}
POST /books
PUT /books/{id}
```

### Authors
```sh
GET /authors
GET /authors/{id}
POST /authors
PUT /authors/{id}
```

### Genres
```sh
GET /genres
GET /genres/{id}
POST /genres
PUT /genres/{id}
```

*All endpoints require Authorization header:*
```sh
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🤖 Optional: AI Book Recommendations (LLM)

This API includes an **optional LLM-powered recommendation system**, run locally via Docker.

### Setup LLM
```sh
cd llm
docker-compose up --build
```

### Use the Recommender Endpoint
```sh
curl -k -X POST https://localhost/api/llm/recomender \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{"text": "Harry Potter"}'
```

Example response:
```json
{
  "results": [
    {
      "title": "Harry Potter and the Philosopher's Stone",
      "author": "J. K. Rowling",
      "year": 1997
    }
  ]
}
```

---

## 💡 Notes

- This project is part of a personal exploration of robust REST architecture and full-stack Java development.
- It serves as a base for testing different frontends, such as Vaadin or Angular, using the same backend.
- It’s also a playground for integrating lightweight AI systems into backend services.

---

## 🧑‍💻 Author

Jessica
[GitHub](https://github.com/JessicaFM)
