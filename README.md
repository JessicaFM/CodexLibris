# 📌 REST API Project with Spring Boot and JWT

This project is a **REST API** built with **Spring Boot**, using **JWT for authentication** and **PostgreSQL** as the database. The application is fully containerized with **Docker**.

---

## 🚀 **How to Start the Project**
### **1️⃣ Build the Docker Image**
Run the following command to build the image without using cache:
```sh
docker-compose build --no-cache
```

### **2️⃣ Start the Services**
```sh
docker-compose up -d
```

### **3️⃣ Check Running Containers**
```sh
docker ps
```

## 🛠 Managing PostgreSQL
### Check Running Services (macOS)
```sh
brew services list
brew services stop postgresql
sudo lsof -i :5432
```

### Check Running Services (Windows)
```sh
netstat -ano | findstr :5432
```

## 🔐 HTTPS Access Notes
The API is served over HTTPS at https://localhost

Since it uses a self-signed certificate, curl commands require the -k flag to skip SSL verification

In a browser, you may need to accept the certificate manually the first time

## 🔥 API Endpoints

### Swager
```sh
https://localhost/swagger-ui/index.html
```

### Login
```sh
curl -X POST https://localhost/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin"}'
```

```sh
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "roleId": 1
}
```


### Users
```sh
curl -k -X GET https://localhost/user/1 \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

```sh
curl -X GET https://localhost/users \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```


### Books
```sh
curl -X GET https://localhost:8080/books \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X GET https://localhost:8080/books/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X POST https://localhost:8080/books \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "title": "Watchmen (Edició Especial)",
          "authorId": 1,
          "isbn": "978-1779501127",
          "publishedDate": "1986-09-01",
          "genreId": 2,
          "available": false
         }'
```

```sh
curl -X PUT https://localhost:8080/books/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "title": "Watchmen (Edició Especial)",
          "authorId": 1,
          "isbn": "978-1779501127",
          "publishedDate": "1986-09-01",
          "genreId": 2,
          "available": false
         }'
```


### Authors
```sh
curl -X GET https://localhost:8080/authors \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X GET https://localhost:8080/authors/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X POST https://localhost:8080/authors \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Alan Moore",
          "birthDate": "1953-11-18",
          "nationality": "Regne Unit"
         }'
```

```sh
curl -X PUT https://localhost:8080/authors/11 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Alan Moore Actualitzat",
          "birthDate": "1953-11-18",
          "nationality": "Regne Unit"
         }'
```

### Genres
```sh
curl -X GET https://localhost:8080/genres \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X GET https://localhost:8080/genres/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X POST https://localhost:8080/genres \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Còmic",
          "description": "Històries explicades mitjançant vinyetes i diàlegs curts."
         }'
```

```sh
curl -X PUT https://localhost:8080/genres/5 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Còmic Actualitzat",
          "description": "Gènere narratiu gràfic, amb vinyetes i diàlegs curts."
         }'
```

## 🧠 Optional: Enable LLM Recommendations
This project includes an optional book recommendation system powered by a local LLM (Large Language Model).

### ▶️ How to Enable LLM Integration
Navigate to the llm/ folder in the project root:
```sh
cd llm
```

Start the LLM service using Docker:
```sh
docker-compose up --build
```

Once running, you can use the LLM-powered recommendation endpoint via:

```sh
curl -k -X POST https://localhost/api/llm/recomender \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
    "text": "Harry Potter"
  }'
```

✅ This will return a list of book recommendations in JSON format, powered by the local AI model.

### Example Response
```json
{
  "results": [
    {
      "title": "Harry Potter and the Philosopher's Stone",
      "author": "J. K. Rowling",
      "year": 1997
    },
    {
      "title": "Harry Potter and the Chamber of Secrets",
      "author": "J. K. Rowling",
      "year": 1998
    }
  ]
}
```
💡 The model may take a few seconds to respond. To improve speed, the prompt and model settings have been optimized.
