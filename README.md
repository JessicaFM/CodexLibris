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

## 🔥 API Endpoints
### Login
```sh
curl -X POST http://localhost:8080/auth/login \
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
curl -X GET http://localhost:8080/user/1 \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

```sh
curl -X GET http://localhost:8080/users \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```


### Books
```sh
curl -X GET http://localhost:8080/books \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X GET http://localhost:8080/books/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X POST http://localhost:8080/books \
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
curl -X PUT http://localhost:8080/books/1 \
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
curl -X GET http://localhost:8080/authors \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X GET http://localhost:8080/authors/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X POST http://localhost:8080/authors \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Alan Moore",
          "birthDate": "1953-11-18",
          "nationality": "Regne Unit"
         }'
```

```sh
curl -X PUT http://localhost:8080/authors/11 \
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
curl -X GET http://localhost:8080/genres \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X GET http://localhost:8080/genres/1 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json"
```

```sh
curl -X POST http://localhost:8080/genres \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Còmic",
          "description": "Històries explicades mitjançant vinyetes i diàlegs curts."
         }'
```

```sh
curl -X PUT http://localhost:8080/genres/5 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
          "name": "Còmic Actualitzat",
          "description": "Gènere narratiu gràfic, amb vinyetes i diàlegs curts."
         }'
```