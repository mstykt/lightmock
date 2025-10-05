#!/usr/bin/env bash

PORT=8087

echo "Starting LightMock..."
java -jar JAR_NAME_HERE.jar up mocks.yaml --port $PORT &
SERVER_PID=$!
sleep 1

echo "Testing GET /hello"
curl -s http://localhost:$PORT/hello
echo -e "\n"

echo "Testing GET /user/42 multiple times (random)"
for i in {1..5}; do
  curl -s http://localhost:$PORT/user/42
  echo
done
echo -e "\n"

echo "Testing POST /login correct body"
curl -s -X POST http://localhost:$PORT/login -H "Content-Type: application/json" -d '{"username":"admin","password":"secret"}'
echo -e "\n"

echo "Testing POST /login incorrect body (should fail with 400)"
curl -s -X POST http://localhost:$PORT/login -H "Content-Type: application/json" -d '{"username":"admin","password":"wrong"}'
echo -e "\n"

echo "Testing POST /feedback"
curl -s -X POST http://localhost:$PORT/feedback
echo -e "\n"

echo "Testing PUT /user/99"
curl -s -X PUT http://localhost:$PORT/user/99
echo -e "\n"

echo "Testing DELETE /user/99"
curl -s -X DELETE http://localhost:$PORT/user/99
echo -e "\n"

echo "Testing PATCH /user/99"
curl -s -X PATCH http://localhost:$PORT/user/99
echo -e "\n"

echo "Stopping LightMock..."
kill $SERVER_PID
wait $SERVER_PID 2>/dev/null
echo "Done."