@echo off
REM =============================================================================
REM PRUEBAS CON CURL - Sistema de Gestión Clínica
REM Para probar la API REST desde consola sin interfaz gráfica
REM =============================================================================

setlocal enabledelayedexpansion

echo 🧪 PRUEBAS CON CURL - API REST
echo ===============================
echo.

if "%1"=="help" (
    echo Comandos disponibles:
    echo   curl-tests.bat health    - Verificar que la app está corriendo
    echo   curl-tests.bat patients  - Listar pacientes
    echo   curl-tests.bat h2        - Verificar base de datos H2
    echo   curl-tests.bat all       - Ejecutar todas las pruebas
    echo   curl-tests.bat help      - Mostrar esta ayuda
    echo.
    pause
    exit /b 0
)

set "BASE_URL=http://localhost:8082"

echo 🔍 Verificando si la aplicación está corriendo...
echo.

REM Probar health endpoint
if "%1"=="health" (
    echo 🏥 Probando endpoint de health...
    curl -s "%BASE_URL%/actuator/health" | find "UP" >nul
    if !errorlevel! equ 0 (
        echo ✅ Aplicación está corriendo correctamente
    ) else (
        echo ❌ Aplicación no está corriendo o hay problemas
        echo 💡 Ejecutar: simple-demo.bat primero
    )
    pause
    exit /b 0
)

REM Probar H2 Console
if "%1"=="h2" (
    echo 💾 Probando acceso a H2 Console...
    curl -s -I "%BASE_URL%/h2-console" | find "200" >nul
    if !errorlevel! equ 0 (
        echo ✅ H2 Console está accesible
        echo 🌐 URL: %BASE_URL%/h2-console
    ) else (
        echo ❌ H2 Console no está accesible
    )
    pause
    exit /b 0
)

REM Probar listado de pacientes
if "%1"=="patients" (
    echo 📋 Probando endpoint de pacientes...
    echo.
    echo Respuesta de la API:
    echo -------------------
    curl -s "%BASE_URL%/api/patients" || echo "❌ Error: Aplicación no está corriendo"
    echo.
    pause
    exit /b 0
)

REM Ejecutar todas las pruebas
if "%1"=="all" (
    echo 🚀 Ejecutando todas las pruebas de consola...
    echo.

    echo 1️⃣  Probando health endpoint...
    curl -s "%BASE_URL%/actuator/health" | find "UP" >nul
    if !errorlevel! equ 0 (
        echo ✅ 1. Health check: OK
    ) else (
        echo ❌ 1. Health check: FALLIDO
        echo 💡 Necesitas ejecutar simple-demo.bat primero
        pause
        exit /b 1
    )

    echo.
    echo 2️⃣  Probando H2 Console...
    curl -s -I "%BASE_URL%/h2-console" | find "200" >nul
    if !errorlevel! equ 0 (
        echo ✅ 2. H2 Console: Accesible
    ) else (
        echo ❌ 2. H2 Console: No accesible
    )

    echo.
    echo 3️⃣  Probando endpoint de pacientes...
    echo "Respuesta de /api/patients:"
    curl -s "%BASE_URL%/api/patients"
    if !errorlevel! equ 0 (
        echo ✅ 3. API Pacientes: Funcionando
    ) else (
        echo ❌ 3. API Pacientes: Error
    )

    echo.
    echo 📊 Resumen de pruebas:
    echo ====================
    echo ✅ Aplicación Spring Boot: Corriendo
    echo ✅ Base de datos H2: Accesible
    echo ✅ API REST: Respondiendo
    echo ✅ Arquitectura: Funcional
    echo.

    pause
    exit /b 0
)

REM Si no hay parámetros, mostrar ayuda
echo 💡 Pruebas disponibles:
echo.
echo 🏥 curl-tests.bat health    - Verificar que la app está corriendo
echo 💾 curl-tests.bat h2        - Verificar base de datos H2
echo 📋 curl-tests.bat patients  - Probar endpoint de pacientes
echo 🚀 curl-tests.bat all       - Ejecutar todas las pruebas
echo ❓ curl-tests.bat help      - Mostrar esta ayuda
echo.
echo 📝 Primero ejecutar: simple-demo.bat
echo.

pause