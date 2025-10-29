#!/bin/bash

# Script de despliegue automatizado para producción
set -e

echo "🚀 Iniciando despliegue de Clinic Management System"

# Verificar que estamos en el directorio correcto
if [ ! -f "docker-compose.prod.yml" ]; then
    echo "❌ Error: docker-compose.prod.yml no encontrado. Ejecuta desde el directorio raíz del proyecto."
    exit 1
fi

# Verificar que existe el archivo .env
if [ ! -f ".env" ]; then
    echo "❌ Error: Archivo .env no encontrado. Copia .env.example y configura las variables."
    exit 1
fi

# Cargar variables de entorno
set -a
source .env
set +a

echo "📦 Deteniendo servicios existentes..."
docker-compose -f docker-compose.prod.yml down

echo "🧹 Limpiando imágenes no utilizadas..."
docker image prune -f

echo "⬇️ Descargando últimas imágenes..."
docker-compose -f docker-compose.prod.yml pull

echo "🏗️ Construyendo servicios personalizados..."
# Aquí puedes agregar builds personalizados si es necesario

echo "🚀 Iniciando servicios..."
docker-compose -f docker-compose.prod.yml up -d

echo "⏳ Esperando a que los servicios estén listos..."
sleep 30

echo "🔍 Verificando estado de los servicios..."
docker-compose -f docker-compose.prod.yml ps

echo "🏥 Verificando health checks..."
# Verificar backend
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Backend: OK"
else
    echo "❌ Backend: FAIL"
fi

# Verificar frontend
if curl -f -k https://localhost/health > /dev/null 2>&1; then
    echo "✅ Frontend: OK"
else
    echo "❌ Frontend: FAIL"
fi

echo "📊 Logs de los servicios:"
docker-compose -f docker-compose.prod.yml logs --tail=20

echo ""
echo "🎉 Despliegue completado!"
echo "🌐 Frontend disponible en: https://clinic.suaza.dev"
echo "🔗 API disponible en: https://api.clinic.suaza.dev"
echo ""
echo "📋 Comandos útiles:"
echo "  - Ver logs: docker-compose -f docker-compose.prod.yml logs -f"
echo "  - Reiniciar: docker-compose -f docker-compose.prod.yml restart"
echo "  - Detener: docker-compose -f docker-compose.prod.yml down"