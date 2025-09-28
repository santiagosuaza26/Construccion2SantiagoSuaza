#!/bin/bash

# Script de inicio rápido para la Clínica CS2
# Uso: ./quick-start.sh

echo "🏥 CLÍNICA CS2 - Inicio Rápido"
echo "=============================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar Java
echo -n "🔍 Verificando Java... "
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java no encontrado${NC}"
    echo -e "${YELLOW}💡 Instala Java 17+ desde: https://adoptium.net/${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt "17" ]; then
    echo -e "${RED}❌ Se requiere Java 17+ (actual: $JAVA_VERSION)${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Java $JAVA_VERSION${NC}"

# Verificar Maven
echo -n "🔍 Verificando Maven... "
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven no encontrado${NC}"
    echo -e "${YELLOW}💡 Instala Maven desde: https://maven.apache.org/${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Maven encontrado${NC}"

# Crear directorio de logs
echo -n "📁 Creando directorio de logs... "
mkdir -p logs 2>/dev/null
echo -e "${GREEN}✅ OK${NC}"

# Compilar proyecto
echo ""
echo "🔨 Compilando proyecto..."
mvn clean package -q

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error durante la compilación${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Compilación exitosa${NC}"

# Iniciar aplicación
echo ""
echo "🚀 Iniciando aplicación..."
echo -e "${YELLOW}📍 Puerto: 8080${NC}"
echo -e "${YELLOW}🔧 Perfil: Desarrollo (H2)${NC}"
echo -e "${YELLOW}📊 Logs: logs/clinic-app.log${NC}"
echo -e "${YELLOW}🌐 API: http://localhost:8080/api${NC}"
echo -e "${YELLOW}🔍 H2 Console: http://localhost:8080/api/h2-console${NC}"
echo ""
echo -e "${GREEN}✅ Aplicación iniciándose en segundo plano...${NC}"
echo -e "${YELLOW}⏳ Espera 10 segundos para que termine de iniciar...${NC}"
echo ""

# Iniciar en background
nohup java -jar \
    -Dspring-boot.run.profiles=dev \
    -Dserver.port=8080 \
    -Dlogging.level.app=INFO \
    target/ProyectoCS2-0.0.1-SNAPSHOT.jar > logs/clinic-app.log 2>&1 &

APP_PID=$!
echo $APP_PID > .app.pid

# Esperar a que la aplicación inicie
sleep 10

# Verificar si la aplicación está respondiendo
echo -n "🔍 Verificando estado de la aplicación... "
if curl -s http://localhost:8080/api/actuator/health | grep -q '"status":"UP"'; then
    echo -e "${GREEN}✅ Aplicación funcionando${NC}"
    echo ""
    echo "🎉 ¡Aplicación lista!"
    echo "====================="
    echo "📖 Consulta el README.md para más información"
    echo "🧪 Usa test-api.http para probar los endpoints"
    echo "📊 Revisa los logs en: logs/clinic-app.log"
    echo ""
    echo -e "${YELLOW}💡 Para detener: kill $APP_PID${NC}"
else
    echo -e "${RED}❌ La aplicación no responde${NC}"
    echo -e "${YELLOW}📊 Revisa los logs: tail -f logs/clinic-app.log${NC}"
fi