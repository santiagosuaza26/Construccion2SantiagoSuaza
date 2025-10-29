# 🚀 Guía de Despliegue - Sistema de Gestión Clínica

## 📋 Tabla de Contenidos

- [🚀 Guía de Despliegue](#-guía-de-despliegue---sistema-de-gestión-clínica)
  - [📋 Tabla de Contenidos](#-tabla-de-contenidos)
  - [🎯 Resumen Ejecutivo](#-resumen-ejecutivo)
  - [🏗️ Arquitectura de Despliegue](#️-arquitectura-de-despliegue)
  - [🔧 Requisitos Previos](#-requisitos-previos)
  - [⚡ Despliegue Rápido con Docker](#-despliegue-rápido-con-docker)
  - [🔐 Configuración de Seguridad](#-configuración-de-seguridad)
  - [📊 Monitoreo y Observabilidad](#-monitoreo-y-observabilidad)
  - [🔄 Estrategias de Backup](#-estrategias-de-backup)
  - [📈 Escalado y Performance](#-escalado-y-performance)
  - [🚨 Plan de Recuperación de Desastres](#-plan-de-recuperación-de-desastres)
  - [🔍 Troubleshooting](#-troubleshooting)
  - [📞 Soporte y Contactos](#-soporte-y-contactos)

## 🎯 Resumen Ejecutivo

Esta guía proporciona instrucciones completas para desplegar el **Sistema de Gestión Clínica** en entornos de producción. El sistema utiliza una arquitectura de microservicios con CI/CD automatizado mediante GitHub Actions.

### **Características Clave del Despliegue:**
- ✅ **Contenedorización completa** con Docker
- ✅ **CI/CD automatizado** con GitHub Actions
- ✅ **Escalabilidad horizontal** y vertical
- ✅ **Alta disponibilidad** con balanceo de carga
- ✅ **Seguridad médica** (HIPAA compliant)
- ✅ **Monitoreo integral** y logging

## 🏗️ Arquitectura de Despliegue

```
┌─────────────────────────────────────────────────────────────────┐
│                    Load Balancer (nginx)                         │
│                                                                 │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   Frontend      │  │   Backend API   │  │   Backend API   │ │
│  │   (nginx)       │  │   (Spring Boot) │  │   (Spring Boot) │ │
│  │                 │  │                 │  │                 │ │
│  │  ┌────────────┐ │  │  ┌────────────┐ │  │  ┌────────────┐ │ │
│  │  │   Static   │ │  │  │ PostgreSQL │ │  │  │ PostgreSQL │ │ │
│  │  │   Files    │ │  │  │   Redis     │ │  │  │   Redis     │ │ │
│  │  └────────────┘ │  │  └────────────┘ │  │  └────────────┘ │ │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                 Shared Storage (MongoDB)                     │ │
│  │              Medical Records & Documents                     │ │
│  └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## 🔧 Requisitos Previos

### **Infraestructura Mínima:**
- **CPU**: 4 cores mínimo, 8 cores recomendado
- **RAM**: 8GB mínimo, 16GB recomendado
- **Almacenamiento**: 50GB SSD mínimo
- **Red**: 100Mbps conexión estable

### **Software Requerido:**
- Docker Engine 20.10+
- Docker Compose 2.0+
- Git 2.30+
- OpenSSL para certificados SSL

### **Puertos Requeridos:**
| Servicio | Puerto | Protocolo | Descripción |
|----------|--------|-----------|-------------|
| Frontend | 80/443 | HTTP/HTTPS | Interfaz de usuario |
| Backend API | 8080 | HTTP | API REST |
| PostgreSQL | 5432 | TCP | Base de datos relacional |
| MongoDB | 27017 | TCP | Base de datos NoSQL |
| Redis | 6379 | TCP | Sistema de caché |

## ⚡ Despliegue Rápido con Docker

### **1. Clonar el Repositorio**

```bash
git clone https://github.com/your-username/clinic-management.git
cd clinic-management
```

### **2. Configurar Variables de Entorno**

```bash
cp .env.example .env
# Editar .env con sus valores reales
nano .env
```

### **3. Despliegue con Docker Compose**

```bash
# Despliegue completo
docker-compose up -d

# Verificar estado de servicios
docker-compose ps

# Ver logs
docker-compose logs -f
```

### **4. Verificar Despliegue**

```bash
# Health check del backend
curl http://localhost:8080/actuator/health

# Health check del frontend
curl http://localhost/health

# Acceder a la aplicación
# Frontend: http://localhost
# API Docs: http://localhost:8080/swagger-ui.html
```

## 🔐 Configuración de Seguridad

### **Certificados SSL/TLS**

```bash
# Generar certificado auto-firmado (desarrollo)
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes

# Para producción, usar Let's Encrypt o certificados comerciales
certbot certonly --webroot -w /var/www/html -d clinic-management.com
```

### **Configuración de Secrets en GitHub**

1. Ir a **Settings > Secrets and variables > Actions**
2. Agregar los siguientes secrets:

```bash
# Base de datos
POSTGRES_PASSWORD=your_secure_password
MONGO_ROOT_PASSWORD=your_mongo_password

# JWT Security
JWT_SECRET=your_256_bit_secret_key

# Docker Registry
DOCKER_USERNAME=your_github_username
DOCKER_PASSWORD=your_github_token

# Opcionales
SLACK_WEBHOOK_URL=https://hooks.slack.com/...
NEW_RELIC_LICENSE_KEY=your_license_key
```

### **Configuración de Firewall**

```bash
# UFW (Ubuntu/Debian)
sudo ufw enable
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp

# Verificar reglas
sudo ufw status
```

## 📊 Monitoreo y Observabilidad

### **Métricas Disponibles**

```bash
# Health endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
curl http://localhost:8080/actuator/metrics

# Logs del contenedor
docker-compose logs backend
docker-compose logs frontend

# Monitoreo de recursos
docker stats
```

### **Configuración de Alertas**

```yaml
# docker-compose.monitoring.yml
version: '3.8'
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3001:3000"
    environment:
      GF_SECURITY_ADMIN_PASSWORD: admin_password
```

## 🔄 Estrategias de Backup

### **Backup Automático de Base de Datos**

```bash
# Script de backup PostgreSQL
#!/bin/bash
BACKUP_DIR="/opt/backups"
DATE=$(date +%Y%m%d_%H%M%S)

# Backup PostgreSQL
docker exec clinic-postgres pg_dump -U clinic_user clinic_management > $BACKUP_DIR/postgres_$DATE.sql

# Backup MongoDB
docker exec clinic-mongodb mongodump --db clinic_history --out $BACKUP_DIR/mongodb_$DATE

# Compresión
tar -czf $BACKUP_DIR/backup_$DATE.tar.gz $BACKUP_DIR/postgres_$DATE.sql $BACKUP_DIR/mongodb_$DATE

# Limpieza (mantener últimos 30 días)
find $BACKUP_DIR -name "backup_*.tar.gz" -mtime +30 -delete
```

### **Programar Backups**

```bash
# Agregar a crontab
crontab -e

# Backup diario a las 2 AM
0 2 * * * /opt/scripts/backup.sh

# Backup semanal los domingos
0 3 * * 0 /opt/scripts/backup-full.sh
```

## 📈 Escalado y Performance

### **Escalado Horizontal**

```yaml
# docker-compose.scale.yml
version: '3.8'
services:
  backend:
    image: clinic-backend:latest
    deploy:
      replicas: 3
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M

  frontend:
    image: clinic-frontend:latest
    deploy:
      replicas: 2
```

### **Optimización de Performance**

```bash
# Configuración de JVM para Spring Boot
JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Configuración de PostgreSQL
shared_buffers = 256MB
effective_cache_size = 1GB
work_mem = 4MB

# Configuración de Redis
maxmemory 512mb
maxmemory-policy allkeys-lru
```

## 🚨 Plan de Recuperación de Desastres

### **Estrategia de Backup**

1. **Backups Diarios**: Base de datos completa
2. **Backups en Tiempo Real**: Logs y configuraciones críticas
3. **Replicación**: Bases de datos en múltiples zonas
4. **Snapshots**: Imágenes de VM completas semanalmente

### **Procedimiento de Recuperación**

```bash
# 1. Detener servicios
docker-compose down

# 2. Restaurar base de datos
docker exec -i clinic-postgres psql -U clinic_user clinic_management < backup.sql

# 3. Restaurar MongoDB
docker exec clinic-mongodb mongorestore /backup/mongodb_backup

# 4. Reiniciar servicios
docker-compose up -d

# 5. Verificar integridad
curl http://localhost:8080/actuator/health
```

### **Tiempos de Recuperación Objetivo (RTO/RPO)**

- **RTO** (Recovery Time Objective): 4 horas
- **RPO** (Recovery Point Objective): 1 hora
- **RTO Crítico**: 1 hora para servicios esenciales

## 🔍 Troubleshooting

### **Problemas Comunes**

#### **Error: Port already in use**
```bash
# Verificar qué proceso usa el puerto
sudo lsof -i :8080

# Matar proceso
sudo kill -9 <PID>

# O cambiar puerto en docker-compose.yml
```

#### **Error: Database connection failed**
```bash
# Verificar estado de PostgreSQL
docker-compose logs postgres

# Conectar manualmente
docker exec -it clinic-postgres psql -U clinic_user -d clinic_management

# Verificar variables de entorno
docker-compose exec backend env | grep DATABASE
```

#### **Error: Out of memory**
```bash
# Verificar uso de memoria
docker stats

# Aumentar límites en docker-compose.yml
services:
  backend:
    deploy:
      resources:
        limits:
          memory: 2G
```

### **Logs y Debugging**

```bash
# Ver todos los logs
docker-compose logs

# Ver logs en tiempo real
docker-compose logs -f backend

# Ver logs de un contenedor específico
docker logs clinic-backend

# Acceder al contenedor para debugging
docker exec -it clinic-backend /bin/bash
```

## 📞 Soporte y Contactos

### **Equipo de Desarrollo**
- **Arquitecto Principal**: Santiago Suaza
- **Email**: santiago.suaza@example.com
- **Repositorio**: https://github.com/your-username/clinic-management

### **Soporte Técnico**
- **Email**: support@clinic-management.com
- **Teléfono**: +57 300 123 4567
- **Horario**: Lunes a Viernes, 8:00 AM - 6:00 PM (GMT-5)

### **Documentación Adicional**
- [README Principal](../README.md)
- [Guía de Configuración](../docs/configuration.md)
- [API Documentation](http://localhost:8080/swagger-ui.html)

---

## 🎯 Checklist de Despliegue

- [ ] Repositorio clonado y configurado
- [ ] Variables de entorno configuradas
- [ ] Secrets de GitHub configurados
- [ ] Docker y Docker Compose instalados
- [ ] Certificados SSL configurados
- [ ] Firewall configurado
- [ ] Servicios desplegados y verificados
- [ ] Backups configurados
- [ ] Monitoreo habilitado
- [ ] Documentación actualizada

**¡Felicitaciones!** 🎉 Tu sistema de gestión clínica está ahora desplegado y listo para usar.

---

*"Tecnología al servicio de la salud - Desarrollado con ❤️ para mejorar vidas"*