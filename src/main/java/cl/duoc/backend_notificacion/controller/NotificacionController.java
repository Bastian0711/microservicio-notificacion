package cl.duoc.backend_notificacion.controller;

import cl.duoc.backend_notificacion.model.Notificacion;
import cl.duoc.backend_notificacion.service.NotificacionService;
import cl.duoc.backend_notificacion.dto.NotificacionCreateDTO;
import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<Notificacion> listarNotificaciones() {
        return notificacionService.listarNotificaciones();
    }

    @PostMapping
    public NotificacionDTO enviarNotificacion(@Valid @RequestBody NotificacionCreateDTO dto) {
        return notificacionService.enviarNotificacion(dto);
    }

    @GetMapping("/{id}")
    public Notificacion buscarPorId(@PathVariable Long id) {
        return notificacionService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Notificacion actualizarNotificacion(
            @PathVariable Long id,
            @Valid @RequestBody Notificacion notificacionActualizada) {

        return notificacionService.actualizarNotificacion(id, notificacionActualizada);
    }

    @PutMapping("/{id}/leer")
    public Notificacion marcarComoLeida(@PathVariable Long id) {
        return notificacionService.marcarComoLeida(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarNotificacion(@PathVariable Long id) {
        boolean eliminado = notificacionService.eliminarNotificacion(id);

        if (eliminado) {
            return "Notificación eliminada correctamente";
        }

        return "Notificación no encontrada";
    }
}
