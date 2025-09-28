#!/bin/bash

# Script de inicio rápido para la API REST de Clínica CS2
echo "🏥 Iniciando API REST - Clínica CS2"
echo "====================================="

# Verificar si Docker está disponible
if command -v docker &> /dev/null && command -v docker-compose &> /dev/null; then
    echo "🐳 Docker detectado. Iniciando con Docker Compose..."

    # Crear archivo .env si no existe
    if [ ! -f .env ]; then
        echo "📄 Creando archivo .env con configuración por defecto..."
        cp .env.example .env
        echo "✅ Archivo .env creado. Puede editarlo para personalizar la configuración."
    fi

    # Construir e iniciar contenedores
    echo "🔨 Construyendo e iniciando contenedores..."
    docker-compose up --build -d

    echo ""
    echo "🚀 API REST iniciada exitosamente!"
    echo ""
    echo "📡 Endpoints disponibles:"
    echo "   • API REST: http://localhost:8080/api"
    echo "   • Documentación: http://localhost:8080/api/swagger-ui.html"
    echo "   • H2 Console: http://localhost:8080/api/h2-console"
    echo "   • Mongo Express: http://localhost:8081"
    echo "   • Adminer: http://localhost:8082"
    echo ""
    echo "🔑 Credenciales de prueba:"
    echo "   • RRHH: carlos123 / Carlos123!"
    echo "   • Admin: maria123 / Maria123!"
    echo "   • Soporte: jorge123 / Jorge123!"
    echo "   • Médico: ana123 / Ana123!"
    echo "   • Enfermera: lucia123 / Lucia123!"
    echo ""
    echo "📊 Bases de datos:"
    echo "   • MySQL: clinicdb_prod (puerto 3306)"
    echo "   • MongoDB: clinical_history_prod (puerto 27017)"
    echo ""
    echo "🛑 Para detener: docker-compose down"
    echo "📋 Para ver logs: docker-compose logs -f app"

else
    echo "🐧 Docker no detectado. Iniciando localmente con Maven..."
    echo ""
    echo "⚠️  Asegúrese de tener:"
    echo "   • Java 17+ instalado"
    echo "   • MySQL corriendo en localhost:3306"
    echo "   • MongoDB corriendo en localhost:27017"
    echo ""
    echo "🔨 Compilando proyecto..."
    ./mvnw clean compile -DskipTests

    echo "🚀 Iniciando aplicación..."
    ./mvnw spring-boot:run
fi