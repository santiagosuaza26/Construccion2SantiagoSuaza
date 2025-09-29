#!/bin/bash

# ===============================================
# SCRIPT DE BACKUP DE BASES DE DATOS
# Sistema de Gestión Médica - Clínica CS2
# ===============================================

set -e  # Detener en caso de error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variables
BACKUP_DIR="../backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
MYSQL_CONTAINER="clinic-mysql"
MONGODB_CONTAINER="clinic-mongodb"
COMPOSE_FILE="../docker-compose.yml"

# Función para logging
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
    exit 1
}

success() {
    echo -e "${GREEN}[SUCCESS] $1${NC}"
}

warning() {
    echo -e "${YELLOW}[WARNING] $1${NC}"
}

# Verificar si Docker Compose está disponible
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null && ! command -v "docker compose" &> /dev/null; then
        error "Docker Compose no está instalado"
    fi
}

# Verificar si contenedores están corriendo
check_containers() {
    log "Verificando estado de contenedores..."

    if ! docker ps | grep -q "$MYSQL_CONTAINER"; then
        error "Contenedor MySQL no está corriendo: $MYSQL_CONTAINER"
    fi

    if ! docker ps | grep -q "$MONGODB_CONTAINER"; then
        error "Contenedor MongoDB no está corriendo: $MONGODB_CONTAINER"
    fi

    success "Todos los contenedores están corriendo"
}

# Crear directorio de backups
create_backup_dir() {
    log "Creando directorio de backups..."

    if [ ! -d "$BACKUP_DIR" ]; then
        mkdir -p "$BACKUP_DIR"
        success "Directorio creado: $BACKUP_DIR"
    else
        success "Directorio ya existe: $BACKUP_DIR"
    fi
}

# Backup de MySQL
backup_mysql() {
    log "Iniciando backup de MySQL..."

    MYSQL_BACKUP_FILE="$BACKUP_DIR/mysql_backup_$TIMESTAMP.sql"

    # Obtener credenciales de .env si existe
    if [ -f "../.env" ]; then
        source ../.env
    fi

    DB_USER=${MYSQL_USER:-clinic_user}
    DB_PASSWORD=${MYSQL_PASSWORD:-secure_password_2024}
    DB_NAME=${MYSQL_DATABASE:-clinicdb}

    docker exec "$MYSQL_CONTAINER" mysqldump \
        -u "$DB_USER" \
        -p"$DB_PASSWORD" \
        "$DB_NAME" > "$MYSQL_BACKUP_FILE"

    if [ $? -eq 0 ]; then
        success "Backup de MySQL completado: $MYSQL_BACKUP_FILE"
        echo "  Tamaño: $(du -h "$MYSQL_BACKUP_FILE" | cut -f1)"
    else
        error "Error en backup de MySQL"
    fi
}

# Backup de MongoDB
backup_mongodb() {
    log "Iniciando backup de MongoDB..."

    MONGODB_BACKUP_DIR="$BACKUP_DIR/mongodb_backup_$TIMESTAMP"

    # Crear directorio temporal en el contenedor
    docker exec "$MONGODB_CONTAINER" mkdir -p /tmp/backup

    # Ejecutar mongodump
    docker exec "$MONGODB_CONTAINER" mongodump \
        --out /tmp/backup \
        --db clinical_history_db

    if [ $? -eq 0 ]; then
        # Copiar backup del contenedor al host
        docker cp "$MONGODB_CONTAINER:/tmp/backup" "$MONGODB_BACKUP_DIR"

        # Limpiar directorio temporal en contenedor
        docker exec "$MONGODB_CONTAINER" rm -rf /tmp/backup

        success "Backup de MongoDB completado: $MONGODB_BACKUP_DIR"
        echo "  Tamaño: $(du -h "$MONGODB_BACKUP_DIR" | cut -f1)"
    else
        error "Error en backup de MongoDB"
    fi
}

# Comprimir backups
compress_backups() {
    log "Comprimiendo backups..."

    ARCHIVE_NAME="clinic_backup_$TIMESTAMP.tar.gz"

    tar -czf "$BACKUP_DIR/$ARCHIVE_NAME" \
        -C "$BACKUP_DIR" \
        "$(basename "$MYSQL_BACKUP_FILE")" \
        "$(basename "$MONGODB_BACKUP_DIR")" 2>/dev/null || true

    if [ $? -eq 0 ] && [ -f "$BACKUP_DIR/$ARCHIVE_NAME" ]; then
        # Remover archivos individuales si la compresión fue exitosa
        rm "$MYSQL_BACKUP_FILE" 2>/dev/null || true
        rm -rf "$MONGODB_BACKUP_DIR" 2>/dev/null || true

        success "Backups comprimidos: $BACKUP_DIR/$ARCHIVE_NAME"
        echo "  Tamaño: $(du -h "$BACKUP_DIR/$ARCHIVE_NAME" | cut -f1)"
    else
        warning "No se pudo comprimir los backups, manteniendo archivos individuales"
    fi
}

# Limpiar backups antiguos
cleanup_old_backups() {
    log "Limpiando backups antiguos (más de 30 días)..."

    find "$BACKUP_DIR" -name "*.sql" -o -name "*backup*" -type f -mtime +30 -delete
    find "$BACKUP_DIR" -name "*.tar.gz" -type f -mtime +30 -delete
    find "$BACKUP_DIR" -type d -empty -delete

    BACKUP_COUNT=$(find "$BACKUP_DIR" -name "*.sql" -o -name "*.tar.gz" | wc -l)
    success "Limpieza completada. Backups actuales: $BACKUP_COUNT"
}

# Generar reporte de backup
generate_report() {
    log "Generando reporte de backup..."

    REPORT_FILE="$BACKUP_DIR/backup_report_$TIMESTAMP.txt"

    {
        echo "========================================="
        echo "REPORTE DE BACKUP - CLÍNICA CS2"
        echo "========================================="
        echo "Fecha: $(date)"
        echo "Timestamp: $TIMESTAMP"
        echo ""
        echo "BACKUPS REALIZADOS:"
        echo "- MySQL: $(ls -lh "$MYSQL_BACKUP_FILE" 2>/dev/null | awk '{print $5}' || echo 'No disponible')"
        echo "- MongoDB: $(du -sh "$MONGODB_BACKUP_DIR" 2>/dev/null | cut -f1 || echo 'No disponible')"
        echo ""
        echo "ESPACIO UTILIZADO:"
        echo "- Total: $(du -sh "$BACKUP_DIR" | cut -f1)"
        echo "- Número de archivos: $(find "$BACKUP_DIR" -type f | wc -l)"
        echo ""
        echo "ESTADO DE SERVICIOS:"
        docker-compose ps --format "table {{.Service}}\t{{.Status}}"
        echo ""
        echo "========================================="
    } > "$REPORT_FILE"

    success "Reporte generado: $REPORT_FILE"
}

# Función principal
main() {
    echo -e "${GREEN}"
    echo "========================================="
    echo "  SISTEMA DE BACKUP - CLÍNICA CS2"
    echo "========================================="
    echo -e "${NC}"

    check_docker_compose
    check_containers
    create_backup_dir
    backup_mysql
    backup_mongodb
    compress_backups
    cleanup_old_backups
    generate_report

    echo -e "${GREEN}"
    echo "========================================="
    echo "  BACKUP COMPLETADO EXITOSAMENTE! ✅"
    echo "========================================="
    echo -e "${NC}"

    echo "Ubicación de backups: $BACKUP_DIR"
    echo "Para restaurar, consulta la documentación en docs/INSTALLATION.md"
}

# Ejecutar función principal
main "$@"