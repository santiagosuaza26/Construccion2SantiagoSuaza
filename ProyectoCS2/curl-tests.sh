#!/bin/bash

# =============================================================================
# PRUEBAS CON CURL - Sistema de Gestión Clínica
# Para probar la API REST desde consola sin interfaz gráfica
# =============================================================================

BASE_URL="http://localhost:8082"

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

show_help() {
    echo -e "${BLUE}🧪 PRUEBAS CON CURL - API REST${NC}"
    echo -e "${BLUE}==============================${NC}"
    echo ""
    echo "Comandos disponibles:"
    echo "  ./curl-tests.sh health    - Verificar que la app está corriendo"
    echo "  ./curl-tests.sh patients  - Listar pacientes"
    echo "  ./curl-tests.sh h2        - Verificar base de datos H2"
    echo "  ./curl-tests.sh all       - Ejecutar todas las pruebas"
    echo "  ./curl-tests.sh help      - Mostrar esta ayuda"
    echo ""
    echo -e "${YELLOW}💡 Primero ejecutar:${NC} ./simple-demo.sh"
    echo ""
}

test_health() {
    echo -e "${YELLOW}🏥 Probando endpoint de health...${NC}"

    if curl -s "$BASE_URL/actuator/health" | grep -q "UP"; then
        echo -e "${GREEN}✅ Aplicación está corriendo correctamente${NC}"
        return 0
    else
        echo -e "${RED}❌ Aplicación no está corriendo o hay problemas${NC}"
        echo -e "${YELLOW}💡 Ejecutar:${NC} ./simple-demo.sh primero${NC}"
        return 1
    fi
}

test_h2() {
    echo -e "${YELLOW}💾 Probando acceso a H2 Console...${NC}"

    if curl -s -I "$BASE_URL/h2-console" | grep -q "200"; then
        echo -e "${GREEN}✅ H2 Console está accesible${NC}"
        echo -e "${BLUE}🌐 URL:${NC} $BASE_URL/h2-console${NC}"
        return 0
    else
        echo -e "${RED}❌ H2 Console no está accesible${NC}"
        return 1
    fi
}

test_patients() {
    echo -e "${YELLOW}📋 Probando endpoint de pacientes...${NC}"
    echo ""
    echo -e "${BLUE}Respuesta de la API:${NC}"
    echo "-------------------"

    if response=$(curl -s "$BASE_URL/api/patients" 2>/dev/null); then
        echo "$response"
        echo ""
        echo -e "${GREEN}✅ API Pacientes: Funcionando${NC}"
        return 0
    else
        echo -e "${RED}❌ Error: Aplicación no está corriendo${NC}"
        return 1
    fi
}

run_all_tests() {
    echo -e "${BLUE}🚀 Ejecutando todas las pruebas de consola...${NC}"
    echo ""

    echo -e "${YELLOW}1️⃣  Probando health endpoint...${NC}"
    if test_health; then
        health_ok=true
    else
        health_ok=false
    fi

    echo ""
    echo -e "${YELLOW}2️⃣  Probando H2 Console...${NC}"
    if test_h2; then
        h2_ok=true
    else
        h2_ok=false
    fi

    echo ""
    echo -e "${YELLOW}3️⃣  Probando endpoint de pacientes...${NC}"
    if test_patients; then
        api_ok=true
    else
        api_ok=false
    fi

    echo ""
    echo -e "${BLUE}📊 Resumen de pruebas:${NC}"
    echo "===================="

    if [ "$health_ok" = true ]; then
        echo -e "${GREEN}✅ Aplicación Spring Boot: Corriendo${NC}"
    else
        echo -e "${RED}❌ Aplicación Spring Boot: No corriendo${NC}"
    fi

    if [ "$h2_ok" = true ]; then
        echo -e "${GREEN}✅ Base de datos H2: Accesible${NC}"
    else
        echo -e "${RED}❌ Base de datos H2: No accesible${NC}"
    fi

    if [ "$api_ok" = true ]; then
        echo -e "${GREEN}✅ API REST: Respondiendo${NC}"
    else
        echo -e "${RED}❌ API REST: Error${NC}"
    fi

    echo ""
    if [ "$health_ok" = true ] && [ "$h2_ok" = true ] && [ "$api_ok" = true ]; then
        echo -e "${GREEN}🎉 ¡Todas las pruebas pasaron!${NC}"
        echo -e "${GREEN}✅ Arquitectura: Funcional${NC}"
        echo -e "${GREEN}✅ Aplicación: Lista para demostración${NC}"
    else
        echo -e "${YELLOW}⚠️ Algunas pruebas fallaron${NC}"
        echo -e "${YELLOW}💡 Ejecutar: ./simple-demo.sh primero${NC}"
    fi
}

# Main logic
case "${1:-help}" in
    "health")
        test_health
        ;;
    "h2")
        test_h2
        ;;
    "patients")
        test_patients
        ;;
    "all")
        run_all_tests
        ;;
    "help"|*)
        show_help
        ;;
esac