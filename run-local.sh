#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

ENV_FILE="$SCRIPT_DIR/env/.env.local"

if [[ ! -f "$ENV_FILE" ]]; then
  echo ""
  echo "Error: no existe '$ENV_FILE'"
  echo "Cree el archivo copiando la plantilla:"
  echo "  cp env/.env.local.example env/.env.local"
  echo "  # luego edite con sus valores reales"
  exit 1
fi

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

echo "Iniciando Orden Trabajo Microservice en local..."
./mvnw spring-boot:run