#!/bin/bash

# Script para configurar SSL con Let's Encrypt
# Uso: ./ssl-setup.sh [dominio]

set -e

DOMAIN=${1:-clinic.suaza.dev}
EMAIL="admin@clinic.suaza.dev"

echo "Configurando SSL para el dominio: $DOMAIN"

# Instalar certbot si no está instalado
if ! command -v certbot &> /dev/null; then
    echo "Instalando certbot..."
    apt-get update
    apt-get install -y certbot
fi

# Crear directorio para certificados
mkdir -p ssl

# Ejecutar certbot para obtener certificado
echo "Obteniendo certificado SSL de Let's Encrypt..."
certbot certonly \
    --standalone \
    --non-interactive \
    --agree-tos \
    --email $EMAIL \
    --domain $DOMAIN \
    --domain www.$DOMAIN

# Copiar certificados al directorio del proyecto
echo "Copiando certificados..."
cp /etc/letsencrypt/live/$DOMAIN/fullchain.pem ssl/clinic.crt
cp /etc/letsencrypt/live/$DOMAIN/privkey.pem ssl/clinic.key

# Configurar renovación automática
echo "Configurando renovación automática..."
cat > /etc/cron.d/certbot-renew << EOF
0 12 * * * root certbot renew --quiet --post-hook "cp /etc/letsencrypt/live/$DOMAIN/fullchain.pem /path/to/project/ssl/clinic.crt && cp /etc/letsencrypt/live/$DOMAIN/privkey.pem /path/to/project/ssl/clinic.key && docker-compose -f docker-compose.prod.yml restart frontend"
EOF

chmod 644 /etc/cron.d/certbot-renew

echo "SSL configurado exitosamente!"
echo "Certificados guardados en: ssl/clinic.crt y ssl/clinic.key"
echo "Recuerda reemplazar '/path/to/project' en el cron job con la ruta real del proyecto."