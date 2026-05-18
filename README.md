# Microservicio Notificacion

## Descripción

Microservicio encargado de gestionar y enviar notificaciones dentro del sistema de eventos gastronómicos.

## Funcionalidades

* Enviar notificaciones
* Gestionar mensajes de notificación
* Comunicación entre microservicios
* Registro de eventos y alertas
* Gestión de mensajes asociados a usuarios

## Tecnologías utilizadas

* Java 21
* Spring Boot
* MySQL
* Maven
* Docker
* Docker Compose
* OpenFeign

## Ejecución del proyecto

```bash id="7krxnt"
docker compose up -d
```

## Endpoints principales

### Obtener notificaciones

GET /api/v2/notificaciones

### Obtener notificación por ID

GET /api/v2/notificaciones/{id}

### Crear notificación

POST /api/v2/notificaciones

### Actualizar notificación

PUT /api/v2/notificaciones/{id}

### Eliminar notificación

DELETE /api/v2/notificaciones/{id}

## Comunicación entre microservicios

Este microservicio permite la integración y comunicación con otros microservicios del sistema para registrar y gestionar notificaciones relacionadas con eventos, pagos y procesos internos.

## Validaciones

* Validación de campos obligatorios
* Manejo de errores controlados
* Bean Validation activa
* Validación de datos recibidos desde otros microservicios
