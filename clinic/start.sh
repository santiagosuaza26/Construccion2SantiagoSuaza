#!/bin/bash

# Script to start Clinic Management System
# Usage: ./start.sh [dev|prod|docker]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[1;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}[CLINIC SYSTEM]${NC} $1"
}

# Default environment
ENVIRONMENT="${1:-dev}"

print_header "Iniciando Sistema de Gestión Clínica"
print_status "Ambiente: $ENVIRONMENT"

# Check if Java is available
if ! command -v java &> /dev/null; then
    print_error "Java 17 is not installed or not in PATH"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Check if Maven is available for dev environment
if [[ "$ENVIRONMENT" == "dev" ]]; then
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        exit 1
    fi
fi

# Check if Docker is available for docker environment
if [[ "$ENVIRONMENT" == "docker" ]]; then
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed or not in PATH"
        exit 1
    fi

    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
        print_error "Docker Compose is not installed or not in PATH"
        exit 1
    fi
fi

case $ENVIRONMENT in
    "dev")
        print_status "Iniciando aplicación en modo desarrollo..."

        # Compile and run with Maven
        print_status "Compilando aplicación..."
        mvn clean compile -Dspring.profiles.active=dev

        print_status "Iniciando aplicación..."
        mvn spring-boot:run -Dspring.profiles.active=dev -Dspring-boot.run.jvmArguments="-Xmx512m -Xms256m"
        ;;

    "prod")
        print_status "Iniciando aplicación en modo producción..."

        # Build the application
        print_status "Construyendo aplicación..."
        mvn clean package -DskipTests -Dspring.profiles.active=prod

        # Find the JAR file
        JAR_FILE=$(find target -name "*.jar" -type f | head -1)

        if [ -z "$JAR_FILE" ]; then
            print_error "No se encontró el archivo JAR construido"
            exit 1
        fi

        print_status "Iniciando aplicación desde: $JAR_FILE"
        java -jar "$JAR_FILE" --spring.profiles.active=prod
        ;;

    "docker")
        print_status "Iniciando aplicación con Docker Compose..."

        # Check if .env file exists for environment variables
        if [ -f ".env" ]; then
            print_status "Cargando variables de entorno desde .env"
        fi

        # Start all services
        print_status "Iniciando servicios de base de datos..."
        docker-compose up -d postgres mongodb redis

        # Wait for databases to be ready
        print_status "Esperando que las bases de datos estén listas..."
        sleep 30

        # Start the application
        print_status "Iniciando aplicación..."
        docker-compose up -d app

        print_status "Sistema iniciado exitosamente!"
        print_status "Aplicación disponible en: http://localhost:8080"
        print_status "Swagger UI disponible en: http://localhost:8080/swagger-ui.html"
        print_status "H2 Console (dev): http://localhost:8080/h2-console"
        print_status ""
        print_status "Para ver logs: docker-compose logs -f"
        print_status "Para detener: docker-compose down"
        ;;

    "test")
        print_status "Ejecutando pruebas..."

        if [ -f "run-tests.sh" ]; then
            chmod +x run-tests.sh
            ./run-tests.sh all
        else
            print_error "Script run-tests.sh no encontrado"
            exit 1
        fi
        ;;

    *)
        print_error "Ambiente no válido: $ENVIRONMENT"
        print_status "Ambientes válidos: dev, prod, docker, test"
        exit 1
        ;;
esac

print_status "¡Sistema iniciado exitosamente!"