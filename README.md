# Microservicio Notificación

## Descripción

Microservicio encargado de gestionar y enviar las notificaciones a los usuarios dentro de **ReadyStand**, una plataforma web para eventos gastronómicos (ferias universitarias, festivales u otras actividades organizadas por instituciones). Cada vez que ocurre un evento relevante para el cliente durante el flujo de compra —pedido confirmado, subpedido en preparación o subpedido listo para retiro— este microservicio registra y despacha el aviso correspondiente.

Dentro de la arquitectura general, Notificación depende del microservicio de **Usuario** (valida que el usuario exista y obtiene su correo como destinatario) y es consumido, a su vez, por los microservicios de **Pedido** (al confirmar un pedido) y **Preparación** (al cambiar el estado de un subpedido, por ejemplo a "listo").

Requerimientos funcionales que cubre este microservicio, según el documento de arquitectura:

| Req. | Descripción |
|---|---|
| R.29 — Notificar subpedido listo | Envía notificación al cliente cada vez que un subpedido cambia a estado "listo", indicando el stand correspondiente. |
| R.30 — Notificar pedido confirmado | Envía notificación al cliente cuando el pedido haya sido confirmado correctamente y haya ingresado al flujo de procesamiento. |
| R.31 — Notificar estado preparación | Informa al cliente sobre cambios relevantes en el estado de sus subpedidos, especialmente al pasar a preparación o a listo. |

> Nota: en el código actual, el envío real del mensaje (correo, push, etc.) no está implementado; el microservicio registra la notificación en base de datos con estado `ENVIADA` y el destinatario resuelto desde Usuario, dejando la entrega efectiva como una capa pendiente de integración (SMTP, colas, etc.).

## Funcionalidades

* Enviar (registrar) una nueva notificación para un usuario
* Listar todas las notificaciones
* Buscar notificación por ID
* Listar notificaciones por ID de usuario
* Actualizar mensaje y/o tipo de una notificación existente
* Marcar una notificación como leída
* Eliminar una notificación
* Comunicación con el microservicio de **Usuario** (vía Feign) para validar existencia y obtener el correo del destinatario
* Registro en Eureka como cliente de descubrimiento de servicios
* Documentación interactiva con Swagger / OpenAPI

## Tecnologías utilizadas

* Java 21
* Spring Boot 3.5.14
* Spring Data JPA
* Spring Cloud OpenFeign
* Spring Cloud Netflix Eureka Client
* MySQL 8.0
* springdoc-openapi (Swagger UI)
* Lombok
* Maven
* Docker / Docker Compose
* H2 (para pruebas)

## Arquitectura y flujo principal

> Dentro del flujo de compra de ReadyStand, este microservicio no es llamado directamente por el cliente final: son los microservicios de **Pedido** y **Preparación** quienes disparan la creación de notificaciones. Los endpoints de consulta, actualización y eliminación descritos aquí son de gestión (por ejemplo, para que el cliente revise su historial o marque avisos como leídos).

### Enviar notificación

1. El cliente (otro microservicio) envía `idUsuario`, `mensaje` y `tipo`.
2. El servicio consulta al usuario en el microservicio de Usuario (`usuario.service.url`) para validar que exista y obtener su correo.
3. Si el usuario no existe, responde `404 NOT_FOUND`. Si el servicio de Usuario no está disponible, responde `503 SERVICE_UNAVAILABLE`.
4. Si el usuario es válido, se crea el registro de notificación con `estado = ENVIADA` y `destinatario` igual al correo obtenido.

### Marcar como leída

1. Se busca la notificación por ID; si no existe, `404 NOT_FOUND`.
2. Si ya está marcada como `LEIDA`, se rechaza con `409 CONFLICT`.
3. Si es válida, se actualiza su estado a `LEIDA`.

## Ejecución del proyecto

> El código fuente vive en `codigo-fuente/backend-notificacion/`. Hay dos formas de levantarlo:

### Opción 1: entorno de desarrollo (contenedor Maven + MySQL)

```bash
docker compose up -d
```

Esto levanta:
* `mysql_servidor_notificacion`: base de datos MySQL en el puerto `3309` (host) → `3306` (contenedor).
* `entorno_notificacion`: contenedor con Maven y JDK 21 montando el código fuente (`./codigo-fuente`) en `/app`, listo para compilar y ejecutar la aplicación manualmente (por ejemplo, con `mvn spring-boot:run` dentro del contenedor). Recibe la variable de entorno `USUARIO_SERVICE_URL` con la URL del microservicio de Usuario.

### Opción 2: imagen de la aplicación (usando el `Dockerfile`)

> Nota: el `docker-compose.yml` no construye ni usa este `Dockerfile`; es una alternativa manual para empaquetar la app en una imagen propia. Ejecutar estos comandos dentro de `codigo-fuente/backend-notificacion/`.

```bash
mvn clean package -DskipTests
docker build -t microservicio-notificacion .
docker run -p 8089:8089 microservicio-notificacion
```

> El servicio escucha en el puerto **8089** (`server.port=8089` en `application.properties`). El `Dockerfile` declara `EXPOSE 8080`, pero esa línea es solo informativa: no coincide con el puerto real de la app, no cambia dónde escucha Spring Boot ni afecta el mapeo con `-p`. Se recomienda corregir ese `EXPOSE` a `8089` para evitar confusiones.

### Configuración relevante

En `application.properties`:

* `spring.datasource.url`: conexión a MySQL (`notificacion_backend`).
* `usuario.service.url`: URL del microservicio de Usuario (consumido internamente en `/api/v3/usuarios/{id}`); puede sobreescribirse con la variable de entorno `USUARIO_SERVICE_URL`.
* `eureka.client.service-url.defaultZone`: URL del servidor Eureka.
* `springdoc.swagger-ui.path=/doc/swagger-ui.html`: ruta de la documentación Swagger.

## Endpoints principales

Base path: `/api/v3/notificaciones`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/v3/notificaciones` | Lista todas las notificaciones |
| GET | `/api/v3/notificaciones/{id}` | Busca una notificación por ID |
| GET | `/api/v3/notificaciones/usuario/{idUsuario}` | Lista las notificaciones de un usuario |
| POST | `/api/v3/notificaciones` | Envía (registra) una nueva notificación |
| PUT | `/api/v3/notificaciones/{id}` | Actualiza mensaje y/o tipo de una notificación |
| PUT | `/api/v3/notificaciones/{id}/leer` | Marca una notificación como leída |
| DELETE | `/api/v3/notificaciones/{id}` | Elimina una notificación |

### Enviar notificación

`POST /api/v3/notificaciones`

```json
{
  "idUsuario": 1,
  "mensaje": "Tu subpedido en el Stand Sushi ya está listo para retirar",
  "tipo": "subpedido_listo"
}
```

### Actualizar notificación

`PUT /api/v3/notificaciones/{id}`

```json
{
  "mensaje": "Tu pedido fue confirmado y está en preparación",
  "tipo": "pedido_confirmado"
}
```

### Marcar como leída

`PUT /api/v3/notificaciones/{id}/leer`

Sin cuerpo de solicitud. Respuesta:

```json
{
  "id": 1,
  "idUsuario": 1,
  "destinatario": "cliente@duoc.cl",
  "mensaje": "Tu subpedido en el Stand Sushi ya está listo para retirar",
  "tipo": "subpedido_listo",
  "estado": "LEIDA"
}
```

## Modelo de datos (respuesta `NotificacionDTO`)

```json
{
  "id": 1,
  "idUsuario": 1,
  "destinatario": "cliente@duoc.cl",
  "mensaje": "Tu pedido fue confirmado y está en preparación",
  "tipo": "pedido_confirmado",
  "estado": "ENVIADA"
}
```

## Validaciones y manejo de errores

* Campos obligatorios en la creación (`idUsuario`, `mensaje`, `tipo`) mediante Bean Validation.
* `idUsuario` debe ser positivo.
* Solo se puede registrar una notificación para un usuario que exista en el microservicio de Usuario.
* No se puede marcar como leída una notificación que ya tiene ese estado.
* No se puede operar sobre una notificación inexistente.

Los errores se devuelven en un formato consistente mediante un manejador global de excepciones:

```json
{
  "error": "NOT_FOUND",
  "message": "Notificación no encontrada"
}
```

| Código de error | HTTP Status | Causa |
|---|---|---|
| `VALIDATION_ERROR` | 400 | Falla de validación de campos (Bean Validation) |
| `NOT_FOUND` | 404 | Notificación o usuario no encontrado |
| `BUSINESS_ERROR` | 409 | Regla de negocio incumplida (notificación ya marcada como leída) |
| `SERVICE_UNAVAILABLE` | 503 | Microservicio de Usuario no disponible |

## Documentación de la API

Con la aplicación en ejecución, la documentación Swagger UI está disponible en:

```
http://localhost:8089/doc/swagger-ui.html
```

## Pruebas

El proyecto incluye pruebas unitarias e de integración para el modelo, repositorio, servicio y controlador (`codigo-fuente/backend-notificacion/src/test/java`), ejecutables con:

```bash
mvn test
```

Las reglas de negocio críticas cubiertas incluyen:

* Solo se registra una notificación para usuarios existentes.
* Si el servicio de Usuario no está disponible, se lanza `ServicioNoDisponibleException`.
* No se puede marcar como leída una notificación ya leída.
* No se puede operar sobre una notificación inexistente.
* Al enviar una notificación, el destinatario se resuelve automáticamente desde el correo del usuario.
