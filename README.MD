# 🚀 LightMock – Lightweight Mock Server

LightMock is a fast, lightweight mock server ideal for API development, test automation, or simulating dependent services. Define your endpoints with JSON or YAML configurations and serve **static** or **random** responses instantly.

---

## ✨ Features

- ⚡ **Fast Startup** – Launch the server with a single command, no extra dependencies required.
- 📄 **JSON & YAML Support** – Flexible configuration formats.
- 🌐 **HTTP Methods** – GET, POST, PUT, DELETE, and more.
- 🎲 **Static & Random Responses**
    - 🟢 Static Response: A single fixed response returned for every request.
    - 🔀 Random Response: Multiple responses can be defined; one is selected randomly per request.
- 🏷 **Path Parameters & Type Checking** – e.g., `{id:int}` or `{name:string}`
- 📝 **Request Body Validation** – Optional `strictBody` enforcement.
- 💻 **CLI Support** – `up`, `list`, `down` commands for server management.
- 📦 **Standalone JAR** – Java runtime is sufficient; no extra setup.
- 🧪 **Test-Friendly** – Suitable for CI/CD pipelines.

---

## ⚙️ Installation

```bash
git clone https://github.com/yourusername/lightmock.git
cd lightmock
mvn clean package

```
---
Generated JAR:

```
target/lightmock-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 🏁 Quick Start

#### 📄 YAML Example

```
# Static Response
- request:
    method: GET
    path: /status
  response:
    - status: 200
      body:
        status: "ok"

# Random Response
- request:
    method: GET
    path: /users/{id:int}
  response:
    - status: 200
      body:
        id: "{{id}}"
        name: "Alice"
    - status: 200
      body:
        id: "{{id}}"
        name: "Bob"

# POST with strictBody
- request:
    method: POST
    path: /login
    body:
      username: "admin"
      password: "1234"
  response:
    - status: 200
      body:
        token: "abcdef123456"
    - status: 401
      body:
        error: "Unauthorized"

# DELETE with no body response
- request:
    method: DELETE
    path: /users/{id:int}
  response:
    - status: 204
      body: ""
```

#### 📄 JSON Example
```
[
  {
    "request": { "method": "GET", "path": "/status" },
    "response": [
      { "status": 200, "body": { "status": "ok" } }
    ]
  },
  {
    "request": { "method": "GET", "path": "/users/{id:int}" },
    "response": [
      { "status": 200, "body": { "id": "{{id}}", "name": "Alice" } },
      { "status": 200, "body": { "id": "{{id}}", "name": "Bob" } }
    ]
  },
  {
    "request": { "method": "POST", "path": "/login", "body": { "username": "admin", "password": "1234" } },
    "response": [
      { "status": 200, "body": { "token": "abcdef123456" } },
      { "status": 401, "body": { "error": "Unauthorized" } }
    ]
  },
  {
    "request": { "method": "DELETE", "path": "/users/{id:int}" },
    "response": [
      { "status": 204, "body": "" }
    ]
  }
]
```
---
## 💻 CLI Usage

```
# Start server with YAML
java -jar target/lightmock-1.0-SNAPSHOT-jar-with-dependencies.jar up mocks.yaml --port 8087

# Start server with JSON
java -jar target/lightmock-1.0-SNAPSHOT-jar-with-dependencies.jar up mocks.json -p 8087

# List endpoints
java -jar target/lightmock-1.0-SNAPSHOT-jar-with-dependencies.jar list

# Stop server
# Use Ctrl+C
```

---

## 🧪 cURL Examples

```
# Static response
curl http://localhost:8087/status
# -> {"status":"ok"}

# Random response
curl http://localhost:8087/users/42
# -> {"id":"42","name":"Alice"} or {"id":"42","name":"Bob"}

# POST login
curl -X POST http://localhost:8087/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"1234"}'

# DELETE user
curl -X DELETE http://localhost:8087/users/99
# -> HTTP 204 No Content
```
---

## ⚙️ Advanced Features
- **🎯 Static vs Random Responses**
  - 🟢 Static: Returns a fixed response.
  - 🔀 Random: Returns one of multiple defined responses randomly.
- **🏷 Path Parameter Types** – e.g., {id:int}, {name:string}
- **📝 Request Body Validation** – strictBody: true/false
- **📦 Content-Type Headers** – Customize response headers
- **🧩 Nested Endpoints** – Support for complex endpoint structures

---

## 📝 Test Script

##### test_lightmock.sh:
```
#!/bin/bash
echo "Testing GET /status"
curl -s http://localhost:8087/status
echo

echo "Testing GET /users/1"
curl -s http://localhost:8087/users/1
echo

echo "Testing POST /login wrong password"
curl -s -X POST http://localhost:8087/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"wrong"}'
echo

echo "Testing DELETE /users/99"
curl -s -X DELETE http://localhost:8087/users/99
echo
```

---

### 🤝 Contributing
1.	Fork the repository
2.	Create a new branch
3.	Commit and push your changes
4.	Open a Pull Request

---

#### 📄 License

MIT License © 2025