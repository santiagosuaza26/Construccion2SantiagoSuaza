#!/bin/bash

# Script de inicio mejorado para el Sistema de Gestión Clínica
# Soluciona problemas comunes de configuración y conexión

echo "🏥 Iniciando Sistema de Gestión Clínica..."
echo "=========================================="

# Verificar si Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker no está corriendo."
    echo "💡 Solución: Inicia Docker Desktop y asegúrate de que esté ejecutándose."
    echo ""
    echo "En Windows:"
    echo "1. Abre Docker Desktop"
    echo "2. Espera a que termine de iniciarse"
    echo "3. Ejecuta este script nuevamente"
    echo ""
    exit 1
fi

echo "✅ Docker está corriendo correctamente"

# Limpiar contenedores anteriores si existen
echo "🧹 Limpiando contenedores anteriores..."
docker-compose down -v > /dev/null 2>&1

# Construir e iniciar servicios
echo "🔨 Construyendo servicios..."
docker-compose build --no-cache > /dev/null 2>&1

echo "🚀 Iniciando servicios..."
docker-compose up -d

# Esperar a que los servicios estén listos
echo "⏳ Esperando a que los servicios estén listos..."
sleep 15

# Verificar estado de los servicios
echo "📊 Verificando estado de servicios..."
docker-compose ps

# Verificar conectividad
echo ""
echo "🔍 Verificando conectividad..."
echo "Backend debería estar disponible en: http://localhost:8080"
echo "Base de datos PostgreSQL en: localhost:5432"
echo "Base de datos MongoDB en: localhost:27017"
echo ""

# Información de acceso
echo "🔑 Credenciales de acceso:"
echo "=========================="
echo "Usuario administrador:"
echo "  Usuario: admin"
echo "  Contraseña: admin123"
echo ""
echo "Usuario Recursos Humanos:"
echo "  Usuario: rrhh"
echo "  Contraseña: rrhh123"
echo ""
echo "Paciente de prueba:"
echo "  Usuario: patient1"
echo "  Contraseña: patient123"
echo ""

# Verificar health check del backend
echo "🏥 Verificando health check del backend..."
if curl -f http://localhost:8080/api/public/health > /dev/null 2>&1; then
    echo "✅ Backend está respondiendo correctamente"
    echo ""
    echo "🎉 ¡Sistema iniciado exitosamente!"
    echo ""
    echo "Puedes acceder al sistema en:"
    echo "🌐 Frontend: Abre index.html en tu navegador"
    echo "🔗 API: http://localhost:8080/api"
    echo "📚 Swagger: http://localhost:8080/swagger-ui.html"
else
    echo "⚠️  El backend podría estar iniciando aún..."
    echo "💡 Si tienes problemas de conexión:"
    echo ""
    echo "1. Espera unos minutos más para que termine de iniciarse"
    echo "2. Verifica que todos los contenedores estén corriendo:"
    echo "   docker-compose ps"
    echo ""
    echo "3. Revisa los logs del backend:"
    echo "   docker-compose logs app"
    echo ""
    echo "4. Si el problema persiste, reinicia los servicios:"
    echo "   docker-compose down -v"
    echo "   docker-compose up --build -d"
fi

echo ""
echo "📝 Notas importantes:"
echo "- El sistema puede tardar hasta 2 minutos en iniciarse completamente"
echo "- Asegúrate de que los puertos 8080, 5432, y 27017 estén disponibles"
echo "- Para detener el sistema: docker-compose down -v"
echo ""