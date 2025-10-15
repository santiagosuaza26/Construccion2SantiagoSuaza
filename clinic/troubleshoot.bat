@echo off
REM Script de solución de problemas para el Sistema de Gestión Clínica
echo.
echo 🏥 Sistema de Gestión Clínica - Solución de Problemas
echo ===================================================
echo.

REM Verificar Docker
echo 🔍 Verificando Docker...
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker no está instalado.
    echo 💡 Solución: Instala Docker Desktop desde https://www.docker.com/products/docker-desktop/
    goto :end
) else (
    echo ✅ Docker está instalado
)

REM Verificar si Docker está corriendo
echo 🔍 Verificando si Docker está corriendo...
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker no está corriendo.
    echo 💡 Solución: Inicia Docker Desktop y asegúrate de que esté completamente iniciado.
    echo.
    echo Pasos:
    echo 1. Abre Docker Desktop
    echo 2. Espera a que termine de iniciarse (puede tomar unos minutos)
    echo 3. Ejecuta este script nuevamente
    goto :end
) else (
    echo ✅ Docker está corriendo
)

REM Verificar puertos
echo 🔍 Verificando puertos disponibles...
netstat -an | findstr :8080 >nul
if not errorlevel 1 (
    echo ⚠️  El puerto 8080 está en uso.
    echo 💡 Solución: Cierra la aplicación que está usando el puerto 8080 o cambia el puerto en docker-compose.yml
)

netstat -an | findstr :5432 >nul
if not errorlevel 1 (
    echo ⚠️  El puerto 5432 está en uso.
    echo 💡 Solución: Cierra PostgreSQL si está corriendo localmente
)

netstat -an | findstr :27017 >nul
if not errorlevel 1 (
    echo ⚠️  El puerto 27017 está en uso.
    echo 💡 Solución: Cierra MongoDB si está corriendo localmente
)

REM Verificar estado de contenedores
echo 🔍 Verificando estado de contenedores...
docker-compose ps -a >nul 2>&1
if errorlevel 1 (
    echo ❌ No se pudo verificar el estado de los contenedores.
    echo 💡 Solución: Asegúrate de estar en el directorio correcto que contiene docker-compose.yml
    goto :end
)

echo.
echo 📊 Estado actual de contenedores:
docker-compose ps

REM Verificar conectividad del backend
echo.
echo 🔍 Verificando conectividad del backend...
curl -f http://localhost:8080/api/public/health >nul 2>&1
if errorlevel 1 (
    echo ❌ El backend no está respondiendo.
    echo 💡 Soluciones posibles:
    echo.
    echo 1. Iniciar servicios:
    echo    .\start-improved.sh
    echo.
    echo 2. Ver logs del backend:
    echo    docker-compose logs app
    echo.
    echo 3. Reiniciar servicios:
    echo    docker-compose down -v
    echo    docker-compose up --build -d
    echo.
    echo 4. Verificar configuración CORS en application-prod.properties
) else (
    echo ✅ El backend está respondiendo correctamente
)

REM Información de acceso
echo.
echo 🔑 Información de acceso para pruebas:
echo =====================================
echo.
echo Usuario administrador:
echo   Usuario: admin
echo   Contraseña: admin123
echo.
echo Usuario Recursos Humanos:
echo   Usuario: rrhh
echo   Contraseña: rrhh123
echo.
echo Paciente de prueba:
echo   Usuario: patient1
echo   Contraseña: patient123
echo.

REM Comandos útiles
echo.
echo 🛠️  Comandos útiles:
echo ===================
echo.
echo Iniciar servicios:
echo   .\start-improved.sh
echo.
echo Ver logs del backend:
echo   docker-compose logs app
echo.
echo Ver logs de todos los servicios:
echo   docker-compose logs
echo.
echo Reiniciar servicios:
echo   docker-compose down -v
echo   docker-compose up --build -d
echo.
echo Detener servicios:
echo   docker-compose down -v
echo.

:end
echo.
echo Presiona cualquier tecla para continuar...
pause >nul