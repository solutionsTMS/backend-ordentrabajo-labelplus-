#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "============================================"
echo "  Deploy - Orden Trabajo Microservice"
echo "============================================"
echo ""
echo "Seleccione el ambiente:"
echo "  1) Desarrollo"
echo "  2) Producción"
echo ""
read -rp "Opción [1-2]: " opcion

case "$opcion" in
  1) ENV_FILE="env/.env.desa" ;;
  2) ENV_FILE="env/.env.prod" ;;
  *)
    echo "Opción inválida."
    exit 1
    ;;
esac

if [[ ! -f "$ENV_FILE" ]]; then
  echo ""
  echo "Error: no existe '$ENV_FILE'"
  echo "Cree el archivo copiando la plantilla correspondiente:"
  echo "  cp env/.env.desa.example env/.env.desa"
  echo "  cp env/.env.prod.example env/.env.prod"
  exit 1
fi

echo ""
echo "Ambiente: $ENV_FILE"
read -rp "¿Continuar con el deploy? [s/N]: " confirmar
if [[ "${confirmar,,}" != "s" ]]; then
  echo "Cancelado."
  exit 0
fi

# Eliminar .env antiguo en raíz: Compose lo auto-carga y puede pisar variables del contenedor.
rm -f .env

# Solo TRAEFIK_* para interpolar labels en docker-compose.yml.
# El env completo de la app va directo al contenedor (sin pasar por .env raíz).
grep -E '^TRAEFIK_' "$ENV_FILE" > .env.compose

export ENV_FILE

# Solo el microservicio; postgresdb y rabbitmq usan profiles y no se despliegan aquí.
SERVICE="orden_trabajo"

PREVIOUS_IMAGE="$(docker compose --env-file .env.compose images -q "$SERVICE" 2>/dev/null || true)"

echo ""
if [[ -n "$PREVIOUS_IMAGE" ]]; then
  echo "Imagen anterior detectada: $PREVIOUS_IMAGE"
  echo "Deteniendo contenedor y eliminando imagen local..."
else
  echo "Deteniendo contenedor..."
fi
docker compose --env-file .env.compose down --rmi local "$SERVICE"

echo "Construyendo imagen con los cambios actuales (sin caché)..."
docker compose --env-file .env.compose build --no-cache "$SERVICE"

echo "Iniciando contenedor..."
docker compose --env-file .env.compose up -d --no-deps --force-recreate "$SERVICE"

echo ""
echo "Deploy completado."
docker compose --env-file .env.compose ps "$SERVICE"

RABBIT_HOST="$(docker exec orden_trabajo_service printenv RABBIT_HOST 2>/dev/null || true)"
if [[ -z "$RABBIT_HOST" ]]; then
  echo ""
  echo "Advertencia: RABBIT_HOST llegó vacío al contenedor."
  echo "Revise que en $ENV_FILE exista la variable RABBIT_HOST."
else
  echo ""
  echo "RABBIT_HOST en contenedor: $RABBIT_HOST"
fi