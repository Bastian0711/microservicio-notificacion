package cl.duoc.backend_notificacion.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.backend_notificacion.dto.NotificacionCreateDTO;
import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import cl.duoc.backend_notificacion.dto.NotificacionUpdateDTO;
import cl.duoc.backend_notificacion.service.NotificacionService;

@RestController
@RequestMapping("/api/v2/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionDTO> listarNotificaciones() {
        return notificacionService.listarNotificaciones();
    }

    @GetMapping("/{id}")
    public NotificacionDTO buscarPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<NotificacionDTO> listarPorUsuario(@PathVariable Long idUsuario) {
        return notificacionService.listarPorUsuario(idUsuario);
    }

    @PostMapping
    public ResponseEntity<NotificacionDTO> enviarNotificacion(@Valid @RequestBody NotificacionCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.enviarNotificacion(dto));
    }

    @PutMapping("/{id}")
    public NotificacionDTO actualizarNotificacion(
            @PathVariable Long id,
            @RequestBody NotificacionUpdateDTO dto) {

        return notificacionService.actualizarNotificacion(id, dto);
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.ok("Notificación eliminada correctamente");
    }
}
