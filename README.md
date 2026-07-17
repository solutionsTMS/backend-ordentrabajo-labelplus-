# Orden de Trabajo — Backend

API REST del módulo de órdenes de trabajo del ERP Etimet. Desarrollada con **Spring Boot 4**, **Java 21**, **PostgreSQL**, autenticación **Keycloak (OAuth2/JWT)** e integración con **RabbitMQ** para sincronizar clientes desde **Taurus**.

Arquitectura hexagonal: `Controller → Service → Contrato ← Repository → Jpa`.

## Requisitos

| Herramienta             | Versión                                             |
| ----------------------- | --------------------------------------------------- |
| Java                    | 21                                                  |
| Maven                   | 3.9+ (incluido `mvnw`)                              |
| PostgreSQL              | 16+                                                 |
| RabbitMQ                | Consumo de eventos de clientes desde Taurus         |
| Docker + Docker Compose | Solo para deploy en servidor o infra local opcional |
| Traefik                 | Red `tms_net` en el servidor                        |

## Estructura del proyecto

```
backend_orden_trabajo/
├── env/
│   ├── .env.local.example    # Plantilla desarrollo local (sin Docker)
│   ├── .env.desa.example     # Plantilla servidor desarrollo
│   └── .env.prod.example     # Plantilla servidor producción
├── reiniciar.sh              # Script de deploy en servidor (desa / prod)
├── run-local.sh              # Script para levantar en local (Linux / macOS)
├── docker-compose.yml
├── Dockerfile
└── src/main/java/com/etimet/orden_trabajo/
    ├── adapters/controllers/
    ├── application/services|mappers/
    ├── domain/               # entities, contratos, records, enums
    ├── exceptions/
    └── infrastructure/       # config, repository, message, specifications
```

## Variables de entorno

Los secretos **no se suben a GitHub**. Cada ambiente tiene su propio archivo, creado a partir de las plantillas `.example`.

| Variable                     | Descripción                                        |
| ---------------------------- | -------------------------------------------------- |
| `SPRING_DATASOURCE_URL`      | URL JDBC de PostgreSQL                             |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos                        |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos                     |
| `KEYCLOAK_ISSUER_URI`        | URI del realm de Keycloak                          |
| `KEYCLOAK_JWK_SET_URI`       | URI del JWK Set de Keycloak                        |
| `RABBIT_HOST`                | Host del servidor RabbitMQ                         |
| `RABBIT_PORT`                | Puerto AMQP (por defecto `5672`)                   |
| `RABBIT_USER`                | Usuario RabbitMQ                                   |
| `RABBIT_PASSWORD`            | Contraseña RabbitMQ                                |
| `RABBIT_VHOST`               | Virtual host (por defecto `/`)                     |
| `RABBIT_SSL_ENABLED`         | `true` / `false` para conexión AMQPS               |
| `SERVER_PORT`                | Puerto HTTP del microservicio (por defecto `8080`) |
| `TRAEFIK_HOST`               | Dominio expuesto por Traefik (solo servidor)       |
| `TRAEFIK_ROUTER`             | Nombre único del router en Traefik (solo servidor) |

### Archivos por ambiente

| Ambiente            | Archivo              | ¿Va a git? |
| ------------------- | -------------------- | ---------- |
| Local (sin Docker)  | `env/.env.local`     | No         |
| Servidor desarrollo | `env/.env.desa`      | No         |
| Servidor producción | `env/.env.prod`      | No         |
| Plantillas          | `env/.env.*.example` | Sí         |

---

## Desarrollo local (sin Docker)

### 1. Clonar el repositorio

```bash
git clone https://github.com/solutionsTMS/backend-ordentrabajo-labelplus-.git
cd backend-ordentrabajo-labelplus-
```

### 2. Crear el archivo de variables

**Linux / macOS:**

```bash
cp env/.env.local.example env/.env.local
```

Editar `env/.env.local` con los valores reales:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/orden_trabajo
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=tu_password

KEYCLOAK_ISSUER_URI=https://desa-sso-etimet.taurus-ts.com/realms/Etimet
KEYCLOAK_JWK_SET_URI=https://desa-sso-etimet.taurus-ts.com/realms/Etimet/protocol/openid-connect/certs

RABBIT_HOST=localhost
RABBIT_PORT=5672
RABBIT_USER=guest
RABBIT_PASSWORD=guest
RABBIT_VHOST=/
RABBIT_SSL_ENABLED=false

SERVER_PORT=8080
```

> Si usas el PostgreSQL del `docker-compose` con perfil `local-db`, el puerto host es **5433**.

### 3. Infraestructura local opcional (Docker)

Solo PostgreSQL en contenedor (RabbitMQ debe estar disponible por separado, local o remoto):

```bash
docker compose --profile local-db up -d
```

### 4. Levantar la aplicación

```bash
chmod +x run-local.sh
./run-local.sh
```

**Alternativa con Maven directamente** (después de exportar las variables manualmente o desde el IDE):

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080/api/`.

### 5. Configurar variables en el IDE (opcional)

En IntelliJ: **Run → Edit Configurations → Environment variables**, agregar las mismas variables de `env/.env.local`.

---

## Deploy en servidor (desa / prod)

El deploy usa Docker y **solo levanta el servicio `orden_trabajo`**. La base de datos y RabbitMQ son **externos** (no se despliegan con este compose en servidor). Traefik expone el servicio en la red `tms_net`.

### Arquitectura en servidor

```
Internet
   │
   ▼
Traefik (red tms_net)
   │  Host: desa-orden-trabajo-etimet.taurus-ts.com
   │  Path: /api/*
   ▼
orden_trabajo_service :8080
   │
   ├──► PostgreSQL externa (fuera de Docker)
   └──► RabbitMQ (desa-rb-etimet.taurus-ts.com)
           ▲
           │ eventos de clientes
        Taurus
```

### 1. Clonar o actualizar el código

```bash
git clone https://github.com/solutionsTMS/backend-ordentrabajo-labelplus-.git
cd backend-ordentrabajo-labelplus-

# En deploys posteriores:
git pull
```

### 2. Crear el archivo de ambiente (solo la primera vez)

**Servidor de desarrollo:**

```bash
cp env/.env.desa.example env/.env.desa
nano env/.env.desa
chmod 600 env/.env.desa
```

**Servidor de producción:**

```bash
cp env/.env.prod.example env/.env.prod
nano env/.env.prod
chmod 600 env/.env.prod
```

Completar con los valores reales. Ejemplo para desa:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://<host-bd>:5432/orden_trabajo
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=<password-bd>

KEYCLOAK_ISSUER_URI=https://desa-sso-etimet.taurus-ts.com/realms/Etimet
KEYCLOAK_JWK_SET_URI=https://desa-sso-etimet.taurus-ts.com/realms/Etimet/protocol/openid-connect/certs

RABBIT_HOST=desa-rb-etimet.taurus-ts.com
RABBIT_PORT=5672
RABBIT_USER=<usuario-rabbit>
RABBIT_PASSWORD=<password-rabbit>
RABBIT_VHOST=/
RABBIT_SSL_ENABLED=false

SERVER_PORT=8080

TRAEFIK_HOST=desa-orden-trabajo-etimet.taurus-ts.com
TRAEFIK_ROUTER=orden-trabajo-microservice-desa
```

> `TRAEFIK_ROUTER` debe ser único en el servidor para no conflictuar con otros backends en el mismo Traefik.
> `SERVER_PORT` debe coincidir con el puerto del load balancer de Traefik (`8080`).

### 3. Ejecutar el deploy

```bash
chmod +x reiniciar.sh
./reiniciar.sh
```

El script:

1. Pregunta el ambiente (**Desarrollo** o **Producción**)
2. Usa `env/.env.desa` o `env/.env.prod` **directamente** en el contenedor
3. Solo extrae `TRAEFIK_*` a `.env.compose` para los labels de Traefik
4. Detiene, reconstruye e inicia **solo** el servicio `orden_trabajo` (`--no-deps`)

> **Importante:** No copie el env completo a `.env` en la raíz. Docker Compose interpreta ese archivo y puede alterar variables con caracteres especiales (por ejemplo, si `RABBIT_PASSWORD` contiene `$`).

### 4. Verificar el deploy

```bash
docker compose --env-file .env.compose ps orden_trabajo
docker logs orden_trabajo_service -f
docker exec orden_trabajo_service printenv RABBIT_HOST
```

**URL de la API en desarrollo:**

```
https://desa-orden-trabajo-etimet.taurus-ts.com/api/
```

---

## Docker Compose — servicios

| Servicio        | Descripción          | ¿Se despliega con `reiniciar.sh`?               |
| --------------- | -------------------- | ----------------------------------------------- |
| `orden_trabajo` | Backend Spring Boot  | Sí                                              |
| `postgresdb`    | PostgreSQL en Docker | No (perfil `local-db`, solo uso local opcional) |

### Levantar PostgreSQL local en Docker (opcional)

```bash
docker compose --profile local-db up -d postgresdb
```

Puerto host: `5433` → contenedor `5432`.

---

## RabbitMQ

Orden de Trabajo **consume** eventos de clientes publicados desde **Taurus** y los persiste localmente.

### Configuración

| Concepto              | Valor                             |
| --------------------- | --------------------------------- |
| Exchange              | `erp.exchange`                    |
| Cola clientes         | `catalogos.taurus.clientes.queue` |
| Routing key           | `taurus.cliente.enviado`          |
| DLQ                   | `catalogos.taurus.clientes.dlq`   |
| Routing key DLQ       | `catalogos.taurus.clientes.dlq.key` |

### Formato de mensajes (desde Taurus)

```json
{
  "accion": "crear",
  "datos": {
    "id": 1,
    "codigoCliente": "C001",
    "identificacionCliente": "1799999999001",
    "razonSocial": "EMPRESA DEMO S.A.",
    "nombreComercial": "DEMO",
    "direccion": "AV. PRINCIPAL 123",
    "zona": "NORTE",
    "provincia": "PICHINCHA",
    "ciudad": "QUITO",
    "parroquia": "CENTRO",
    "telefono": "022222222",
    "celular": "0999999999",
    "email": "demo@empresa.com",
    "dias": 30,
    "sexo": "M",
    "estadoCivil": "S",
    "nombreUsuario": "taurus",
    "estadoCliente": true,
    "calificacion": 5
  }
}
```

Acciones soportadas: `crear`, `actualizar`, `eliminar`.

> `eliminar` hace soft delete (`estadoCliente = false`). Acciones desconocidas se procesan como crear/actualizar.

---

## API

Módulos expuestos bajo `/api/`. Requieren token JWT válido emitido por Keycloak (realm `Etimet`).

| Módulo               | Ruta base                   | Descripción                                      |
| -------------------- | --------------------------- | ------------------------------------------------ |
| Clientes             | `/api/clientes`             | CRUD + activar; sincronizable vía RabbitMQ       |
| Productos terminados | `/api/productos-terminados` | CRUD + activar; filtro por `clienteId`           |
| Órdenes de trabajo   | `/api/ordenes-trabajo`      | CRUD; anulación vía DELETE (estado `Anulado`)    |

### Endpoints principales

**Clientes** (`/api/clientes`)

| Método | Ruta            | Descripción        |
| ------ | --------------- | ------------------ |
| POST   | `/`             | Crear              |
| PUT    | `/{id}`         | Actualizar         |
| DELETE | `/{id}`         | Soft delete        |
| PUT    | `/{id}/activar` | Activar            |
| GET    | `/{id}`         | Obtener por id     |
| GET    | `/?buscar=`     | Listado paginado   |

**Productos terminados** (`/api/productos-terminados`)

| Método | Ruta                  | Descripción              |
| ------ | --------------------- | ------------------------ |
| POST   | `/`                   | Crear                    |
| PUT    | `/{id}`               | Actualizar               |
| DELETE | `/{id}`               | Soft delete              |
| PUT    | `/{id}/activar`       | Activar                  |
| GET    | `/{id}`               | Obtener por id           |
| GET    | `/?clienteId=`        | Listar por cliente       |
| GET    | `/?buscar=`           | Listado paginado         |

**Órdenes de trabajo** (`/api/ordenes-trabajo`)

| Método | Ruta        | Descripción              |
| ------ | ----------- | ------------------------ |
| POST   | `/`         | Crear                    |
| PUT    | `/{id}`     | Actualizar               |
| DELETE | `/{id}`     | Anular                   |
| GET    | `/{id}`     | Obtener por id           |
| GET    | `/?buscar=` | Listado paginado         |

### Enums

| Campo       | Valores                                      |
| ----------- | -------------------------------------------- |
| Modalidad   | `Nuevo`, `Repetido`                          |
| TipoTrabajo | `Etiqueta`, `Rollo`, `Hojas`, `Termoencogible` |
| Estado OT   | `Pendiente`, `Revision`, `Aprobada`, `Anulada` |

### Ejemplo — crear orden de trabajo

`POST /api/ordenes-trabajo`

```json
{
  "clienteId": 1,
  "modalidad": "Nuevo",
  "tipoTrabajo": "Etiqueta",
  "productoId": null,
  "cantidadFabricar": 1000,
  "observacion": "Orden de prueba",
  "avance": 10.5,
  "ancho": 50.0,
  "lyflat": 20.0,
  "formaEtiqueta": "RECTANGULAR",
  "prepicadoEspecial": false,
  "corteSuperficial": false,
  "esImpreso": true,
  "arteAdjuntoNombre": "arte-cliente.pdf",
  "acabados": "Barniz",
  "colores": "CMYK"
}
```

### Ejemplo — crear producto terminado

`POST /api/productos-terminados`

```json
{
  "codigo": "PT-001",
  "nombre": "ETIQUETA DEMO",
  "clienteId": 1,
  "tipoTrabajo": "Etiqueta",
  "datosTecnicos": "Material: papel couché",
  "avance": 10.5,
  "ancho": 50.0,
  "lyflat": 20.0,
  "formaEtiqueta": "RECTANGULAR"
}
```

---

## Seguridad

- Los archivos `env/.env.local`, `env/.env.desa`, `env/.env.prod` y `.env` están en `.gitignore` y **nunca deben subirse al repositorio**.
- Solo las plantillas `*.example` van a git.
- En el servidor, asignar permisos restrictivos: `chmod 600 env/.env.*`
- Si alguna credencial estuvo expuesta en git, **rotarla** en el servicio correspondiente.

---

## Comandos útiles

```bash
# Ver logs del contenedor en servidor
docker logs orden_trabajo_service -f

# Reiniciar sin rebuild
docker compose --env-file .env.compose restart orden_trabajo

# Compilar sin ejecutar tests
./mvnw clean package -DskipTests

# Detener el servicio en servidor
docker compose --env-file .env.compose down orden_trabajo

# Liberar puerto 8080 en local (macOS / Linux)
lsof -ti :8080 | xargs kill -9
```
