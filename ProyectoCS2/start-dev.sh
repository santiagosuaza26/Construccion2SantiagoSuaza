#!/bin/bash

# Script de inicialización para desarrollo
# Uso: ./start-dev.sh [puerto]

echo "🏥 Iniciando Sistema de Gestión Médica - Modo Desarrollo"
echo "=================================================="

# Puerto por defecto
PORT=${1:-8080}

echo "📍 Puerto: $PORT"
echo "🔧 Perfil: dev (H2 Database)"
echo ""

# Verificar si Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java no está instalado"
    echo "💡 Instala Java 17 o superior: https://adoptium.net/"
    exit 1
fi

# Verificar versión de Java
JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt "17" ]; then
    echo "❌ Error: Se requiere Java 17 o superior (actual: $JAVA_VERSION)"
    exit 1
fi

echo "✅ Java $JAVA_VERSION detectado"

# Crear directorio de logs si no existe
mkdir -p logs

# Compilar el proyecto si no está compilado
if [ ! -f "target/ProyectoCS2-0.0.1-SNAPSHOT.jar" ]; then
    echo "🔨 Compilando proyecto..."
    mvn clean package -DskipTests

    if [ $? -ne 0 ]; then
        echo "❌ Error durante la compilación"
        exit 1
    fi
fi

echo "🚀 Iniciando aplicación..."
echo "📊 Logs disponibles en: logs/clinic-app.log"
echo "🌐 URL: http://localhost:$PORT/api"
echo "🔍 H2 Console: http://localhost:$PORT/api/h2-console"
echo ""
echo "Presiona Ctrl+C para detener la aplicación"
echo ""

# Iniciar aplicación
java -jar \
    -Dspring-boot.run.profiles=dev \
    -Dserver.port=$PORT \
    -Dlogging.level.app=INFO \
    target/ProyectoCS2-0.0.1-SNAPSHOT.jar