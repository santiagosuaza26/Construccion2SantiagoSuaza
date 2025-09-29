#!/bin/bash

# Script para probar los endpoints de la aplicación
# Uso: ./test-endpoint.sh

BASE_URL="http://localhost:8081/api"

echo "🚀 Probando endpoints de la aplicación..."
echo "============================================="

# 1. Health Check
echo "1️⃣  Probando Health Check..."
curl -s -w "\nStatus: %{http_code}\n" \
  "$BASE_URL/auth/health" | head -10

echo -e "\n============================================="

# 2. Probar endpoint con datos de prueba
echo "2️⃣  Probando endpoint de autenticación..."
curl -s -X POST \
  "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "password": "test"
  }' \
  -w "\nStatus: %{http_code}\n" | head -10

echo -e "\n============================================="

# 3. Probar endpoint médico (debería fallar sin autenticación)
echo "3️⃣  Probando endpoint médico sin autenticación..."
curl -s -X GET \
  "$BASE_URL/medical/clinical-history/12345678" \
  -w "\nStatus: %{http_code}\n" | head -10

echo -e "\n============================================="
echo "✅ Pruebas completadas"
echo ""
echo "💡 Para ver logs de la aplicación:"
echo "   tail -f logs/spring-boot-app.log"
echo ""
echo "💡 Para reiniciar la aplicación:"
echo "   mvn spring-boot:run"