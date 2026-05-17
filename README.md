# Microservicio Notificacion

## Descripción

Microservicio encargado de gestionar y enviar notificaciones dentro del sistema de eventos gastronómicos.

## Funcionalidades

* Enviar notificaciones
* Gestionar mensajes de notificación
* Comunicación entre microservicios
* Registro de eventos y alertas

## Tecnologías utilizadas

* Java 21
* Spring Boot
* MySQL
* Maven
* Docker
* Docker Compose

## Ejecución del proyecto

```bash id="5v4csl"
docker compose up -d
```

## Endpoints principales

### Obtener notificaciones

GET /api/v1/notificaciones

### Crear notificación

POST /api/v1/notificaciones

### Actualizar notificación

PUT /api/v1/notificaciones/{id}

### Eliminar notificación

DELETE /api/v1/notificaciones/{id}

## Validaciones

* Validación de campos obligatorios
* Manejo de errores controlados
* Bean Validation activa
