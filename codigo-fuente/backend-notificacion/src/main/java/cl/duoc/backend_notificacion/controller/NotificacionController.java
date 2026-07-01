package cl.duoc.backend_notificacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.backend_notificacion.dto.NotificacionCreateDTO;
import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import cl.duoc.backend_notificacion.dto.NotificacionUpdateDTO;
import cl.duoc.backend_notificacion.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones relacionadas con notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Listar todas las notificaciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<NotificacionDTO> listarNotificaciones() {
        return notificacionService.listarNotificaciones();
    }

    @Operation(summary = "Buscar notificación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public NotificacionDTO buscarPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id);
    }

    @Operation(summary = "Listar notificaciones por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public List<NotificacionDTO> listarPorUsuario(@PathVariable Long idUsuario) {
        return notificacionService.listarPorUsuario(idUsuario);
    }

    @Operation(summary = "Enviar una nueva notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación enviada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<NotificacionDTO> enviarNotificacion(@Valid @RequestBody NotificacionCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.enviarNotificacion(dto));
    }

    @Operation(summary = "Actualizar una notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación actualizada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}")
    public NotificacionDTO actualizarNotificacion(@PathVariable Long id, @RequestBody NotificacionUpdateDTO dto) {
        return notificacionService.actualizarNotificacion(id, dto);
    }

    @Operation(summary = "Marcar notificación como leída")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @Operation(summary = "Eliminar una notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación eliminada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.ok("Notificación eliminada correctamente");
    }
}