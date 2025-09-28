# Script de inicio rápido para Windows - API REST de Clínica CS2
Write-Host "🏥 Iniciando API REST - Clínica CS2" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green

# Verificar si Docker Desktop está disponible
$dockerAvailable = Get-Command docker -ErrorAction SilentlyContinue
$dockerComposeAvailable = Get-Command docker-compose -ErrorAction SilentlyContinue

if ($dockerAvailable -and $dockerComposeAvailable) {
    Write-Host "🐳 Docker detectado. Iniciando con Docker Compose..." -ForegroundColor Cyan

    # Crear archivo .env si no existe
    if (-not (Test-Path .env)) {
        Write-Host "📄 Creando archivo .env con configuración por defecto..." -ForegroundColor Yellow
        Copy-Item .env.example .env
        Write-Host "✅ Archivo .env creado. Puede editarlo para personalizar la configuración." -ForegroundColor Green
    }

    # Construir e iniciar contenedores
    Write-Host "🔨 Construyendo e iniciando contenedores..." -ForegroundColor Yellow
    docker-compose up --build -d

    Write-Host ""
    Write-Host "🚀 API REST iniciada exitosamente!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📡 Endpoints disponibles:" -ForegroundColor Cyan
    Write-Host "   • API REST: http://localhost:8080/api" -ForegroundColor White
    Write-Host "   • Documentación: http://localhost:8080/api/swagger-ui.html" -ForegroundColor White
    Write-Host "   • H2 Console: http://localhost:8080/api/h2-console" -ForegroundColor White
    Write-Host "   • Mongo Express: http://localhost:8081" -ForegroundColor White
    Write-Host "   • Adminer: http://localhost:8082" -ForegroundColor White
    Write-Host ""
    Write-Host "🔑 Credenciales de prueba:" -ForegroundColor Cyan
    Write-Host "   • RRHH: carlos123 / Carlos123!" -ForegroundColor White
    Write-Host "   • Admin: maria123 / Maria123!" -ForegroundColor White
    Write-Host "   • Soporte: jorge123 / Jorge123!" -ForegroundColor White
    Write-Host "   • Médico: ana123 / Ana123!" -ForegroundColor White
    Write-Host "   • Enfermera: lucia123 / Lucia123!" -ForegroundColor White
    Write-Host ""
    Write-Host "📊 Bases de datos:" -ForegroundColor Cyan
    Write-Host "   • MySQL: clinicdb_prod (puerto 3306)" -ForegroundColor White
    Write-Host "   • MongoDB: clinical_history_prod (puerto 27017)" -ForegroundColor White
    Write-Host ""
    Write-Host "🛑 Para detener: docker-compose down" -ForegroundColor Yellow
    Write-Host "📋 Para ver logs: docker-compose logs -f app" -ForegroundColor Yellow

} else {
    Write-Host "🐧 Docker no detectado. Iniciando localmente con Maven..." -ForegroundColor Cyan
    Write-Host ""
    Write-Host "⚠️  Asegúrese de tener:" -ForegroundColor Yellow
    Write-Host "   • Java 17+ instalado" -ForegroundColor White
    Write-Host "   • MySQL corriendo en localhost:3306" -ForegroundColor White
    Write-Host "   • MongoDB corriendo en localhost:27017" -ForegroundColor White
    Write-Host ""
    Write-Host "🔨 Compilando proyecto..." -ForegroundColor Yellow
    & .\mvnw.cmd clean compile -DskipTests

    Write-Host "🚀 Iniciando aplicación..." -ForegroundColor Green
    & .\mvnw.cmd spring-boot:run
}

Write-Host ""
Write-Host "🏥 ¡API REST de Clínica CS2 lista para usar!" -ForegroundColor Green