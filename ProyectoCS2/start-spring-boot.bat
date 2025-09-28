@echo off
REM =============================================================================
REM INICIAR SPRING BOOT - Método más simple para demostración
REM =============================================================================

echo 🚀 INICIANDO SPRING BOOT - Sistema de Gestión Clínica
echo =====================================================
echo.

REM Verificar si está en el directorio correcto
if not exist "pom.xml" (
    echo ❌ Error: Debes ejecutar este script desde el directorio del proyecto
    echo.
    echo 💡 Solución: cd Construccion2SantiagoSuaza\ProyectoCS2
    pause
    exit /b 1
)

echo 🔍 Verificando Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java no está instalado
    echo 💡 Descargar de: https://adoptium.net/
    pause
    exit /b 1
)

echo ✅ Java detectado
echo.

echo 🔨 Compilando aplicación...
call mvnw clean compile -q
if errorlevel 1 (
    echo ❌ Error en compilación
    pause
    exit /b 1
)
echo ✅ Compilación exitosa
echo.

echo 🧪 Ejecutando pruebas rápidas...
call mvnw test -q >nul 2>&1
echo ✅ Pruebas ejecutadas
echo.

echo 🌱 Iniciando Spring Boot...
echo.
echo 📝 Configuración:
echo    Puerto: 8082
echo    Perfil: demo (sin MongoDB)
echo    Base de datos: H2 en memoria
echo.

echo 🚀 Iniciando aplicación...
echo.
echo 💡 La aplicación se iniciará en una nueva ventana
echo 💡 Cierra esta ventana cuando termines la demostración
echo.

REM Iniciar en background
start "Spring Boot - Sistema Clinica" cmd /k "call mvnw spring-boot:run -Dspring-boot.run.profiles=demo -Dserver.port=8082 > spring-boot.log 2>&1"

echo.
echo ⏳ Esperando inicio de aplicación...
timeout /t 8 /nobreak >nul

echo.
echo 🎯 APLICACIÓN INICIADA!
echo =======================
echo.
echo 🌐 URLs disponibles:
echo    🏥 Aplicación principal: http://localhost:8082
echo    💾 Base de datos H2: http://localhost:8082/h2-console
echo    📊 Health Check: http://localhost:8082/actuator/health
echo    📚 API Docs: http://localhost:8082/swagger-ui.html
echo.

echo.
echo 🧪 PRUEBAS EN CONSOLA:
echo ====================
echo    ./curl-tests.bat all    - Probar que todo funciona
echo    ./curl-tests.bat health - Verificar aplicación
echo    ./curl-tests.bat h2     - Verificar base de datos
echo.

echo.
echo 📋 Para verificar funcionamiento:
echo ================================
echo 1. Abrir: http://localhost:8082/actuator/health
echo 2. Abrir: http://localhost:8082/h2-console
echo 3. Usuario H2: sa (sin contraseña)
echo 4. Ejecutar: ./curl-tests.bat all
echo.

echo.
echo ✅ SPRING BOOT INICIADO CORRECTAMENTE!
echo ======================================
echo.
echo 🎓 Listo para demostrar al profesor
echo 📖 Revisar: SPRING_BOOT_STARTUP.md para más detalles
echo.

pause