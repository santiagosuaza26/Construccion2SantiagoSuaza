@echo off
REM Script de inicialización para desarrollo en Windows
REM Uso: start-dev.bat [puerto]

echo 🏥 Iniciando Sistema de Gestión Médica - Modo Desarrollo
echo ==================================================
echo.

REM Puerto por defecto
if "%1"=="" (
    set PORT=8080
) else (
    set PORT=%1
)

echo 📍 Puerto: %PORT%
echo 🔧 Perfil: dev (H2 Database)
echo.

REM Verificar Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Error: Java no está instalado
    echo 💡 Descarga Java 17 o superior desde: https://adoptium.net/
    pause
    exit /b 1
)

REM Obtener versión de Java
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i version') do (
    set JAVA_VERSION=%%i
)

REM Extraer solo el número mayor
set JAVA_VERSION=%JAVA_VERSION:"=%
for /f "delims=." %%i in ("%JAVA_VERSION%") do set MAJOR_VERSION=%%i

if %MAJOR_VERSION% LSS 17 (
    echo ❌ Error: Se requiere Java 17 o superior (actual: %MAJOR_VERSION%)
    pause
    exit /b 1
)

echo ✅ Java %MAJOR_VERSION% detectado
echo.

REM Crear directorio de logs
if not exist logs mkdir logs

REM Compilar proyecto si no existe el JAR
if not exist "target\ProyectoCS2-0.0.1-SNAPSHOT.jar" (
    echo 🔨 Compilando proyecto...
    call mvn clean package -DskipTests

    if errorlevel 1 (
        echo ❌ Error durante la compilación
        pause
        exit /b 1
    )
)

echo 🚀 Iniciando aplicación...
echo 📊 Logs disponibles en: logs\clinic-app.log
echo 🌐 URL: http://localhost:%PORT%/api
echo 🔍 H2 Console: http://localhost:%PORT%/api/h2-console
echo.
echo Presiona Ctrl+C para detener la aplicación
echo.

REM Iniciar aplicación
java -jar ^
    -Dspring-boot.run.profiles=dev ^
    -Dserver.port=%PORT% ^
    -Dlogging.level.app=INFO ^
    target\ProyectoCS2-0.0.1-SNAPSHOT.jar