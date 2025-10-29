#!/bin/bash

# Script para configurar monitoring básico con Prometheus y Grafana
set -e

echo "📊 Configurando monitoring básico..."

# Crear directorio para configuración de monitoring
mkdir -p monitoring/prometheus
mkdir -p monitoring/grafana/provisioning/datasources
mkdir -p monitoring/grafana/provisioning/dashboards
mkdir -p monitoring/grafana/dashboards

# Configuración de Prometheus
cat > monitoring/prometheus/prometheus.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

scrape_configs:
  - job_name: 'clinic-backend'
    static_configs:
      - targets: ['backend:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s

  - job_name: 'clinic-frontend'
    static_configs:
      - targets: ['frontend:80']
    scrape_interval: 30s

  - job_name: 'postgres'
    static_configs:
      - targets: ['postgres:5432']
    scrape_interval: 30s

  - job_name: 'redis'
    static_configs:
      - targets: ['redis:6379']
    scrape_interval: 30s

  - job_name: 'mongo'
    static_configs:
      - targets: ['mongo:27017']
    scrape_interval: 30s
EOF

# Configuración de Grafana - Datasource
cat > monitoring/grafana/provisioning/datasources/prometheus.yml << 'EOF'
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true
    editable: true
EOF

# Dashboard de ejemplo para Spring Boot
cat > monitoring/grafana/dashboards/spring-boot-dashboard.json << 'EOF'
{
  "dashboard": {
    "title": "Clinic Management System - Spring Boot Metrics",
    "tags": ["clinic", "spring-boot"],
    "timezone": "browser",
    "panels": [
      {
        "title": "JVM Memory",
        "type": "graph",
        "targets": [
          {
            "expr": "jvm_memory_used_bytes{job=\"clinic-backend\"}",
            "legendFormat": "{{area}}"
          }
        ]
      },
      {
        "title": "HTTP Requests",
        "type": "graph",
        "targets": [
          {
            "expr": "http_server_requests_seconds_count{job=\"clinic-backend\"}",
            "legendFormat": "{{method}} {{uri}}"
          }
        ]
      }
    ],
    "time": {
      "from": "now-1h",
      "to": "now"
    },
    "refresh": "5s"
  }
}
EOF

# Configuración de dashboards para Grafana
cat > monitoring/grafana/provisioning/dashboards/dashboard.yml << 'EOF'
apiVersion: 1

providers:
  - name: 'default'
    type: file
    disableDeletion: false
    updateIntervalSeconds: 10
    allowUiUpdates: true
    options:
      path: /var/lib/grafana/dashboards
EOF

# Docker Compose para monitoring
cat > docker-compose.monitoring.yml << 'EOF'
version: '3.8'

services:
  prometheus:
    image: prom/prometheus:latest
    container_name: clinic_prometheus
    restart: unless-stopped
    ports:
      - "9090:9090"
    volumes:
      - ./monitoring/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro
      - prometheus_data:/prometheus
    networks:
      - clinic_network
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--storage.tsdb.retention.time=200h'
      - '--web.enable-lifecycle'

  grafana:
    image: grafana/grafana:latest
    container_name: clinic_grafana
    restart: unless-stopped
    ports:
      - "3001:3000"
    environment:
      GF_SECURITY_ADMIN_PASSWORD: admin123
      GF_USERS_ALLOW_SIGN_UP: false
    volumes:
      - grafana_data:/var/lib/grafana
      - ./monitoring/grafana/provisioning:/etc/grafana/provisioning:ro
      - ./monitoring/grafana/dashboards:/var/lib/grafana/dashboards:ro
    networks:
      - clinic_network
    depends_on:
      - prometheus

  node-exporter:
    image: prom/node-exporter:latest
    container_name: clinic_node_exporter
    restart: unless-stopped
    ports:
      - "9100:9100"
    volumes:
      - /proc:/host/proc:ro
      - /sys:/host/sys:ro
      - /:/rootfs:ro
    networks:
      - clinic_network
    command:
      - '--path.procfs=/host/proc'
      - '--path.rootfs=/rootfs'
      - '--path.sysfs=/host/sys'
      - '--collector.filesystem.mount-points-exclude=^/(sys|proc|dev|host|etc)($$|/)'

volumes:
  prometheus_data:
  grafana_data:

networks:
  clinic_network:
    external: true
    name: construccion2santiagosuaza_clinic_network
EOF

echo "✅ Configuración de monitoring completada!"
echo ""
echo "🚀 Para iniciar monitoring:"
echo "  docker-compose -f docker-compose.monitoring.yml up -d"
echo ""
echo "📊 Acceder a:"
echo "  - Prometheus: http://localhost:9090"
echo "  - Grafana: http://localhost:3001 (admin/admin123)"
echo ""
echo "🔗 Integrar con producción:"
echo "  docker-compose -f docker-compose.prod.yml -f docker-compose.monitoring.yml up -d"