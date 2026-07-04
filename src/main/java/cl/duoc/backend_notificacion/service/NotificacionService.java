package cl.duoc.backend_notificacion.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.backend_notificacion.client.UsuarioClient;
import cl.duoc.backend_notificacion.dto.NotificacionCreateDTO;
import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import cl.duoc.backend_notificacion.dto.NotificacionUpdateDTO;
import cl.duoc.backend_notificacion.dto.UsuarioDTO;
import cl.duoc.backend_notificacion.exception.EstadoInvalidoException;
import cl.duoc.backend_notificacion.exception.RecursoNoEncontradoException;
import cl.duoc.backend_notificacion.exception.ServicioNoDisponibleException;
import cl.duoc.backend_notificacion.model.Notificacion;
import cl.duoc.backend_notificacion.repository.NotificacionRepository;
import feign.FeignException;

@Service
public class NotificacionService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository repository;
    private final UsuarioClient usuarioClient;

    public NotificacionService(
            NotificacionRepository repository,
            UsuarioClient usuarioClient) {

        this.repository = repository;
        this.usuarioClient = usuarioClient;
    }

    public List<NotificacionDTO> listarNotificaciones() {

        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public NotificacionDTO obtenerPorId(Long id) {
        return toDto(obtenerEntidadPorId(id));
    }

    public List<NotificacionDTO> listarPorUsuario(Long idUsuario) {

        return repository.findByIdUsuario(idUsuario)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public NotificacionDTO enviarNotificacion(NotificacionCreateDTO dto) {

        UsuarioDTO usuario = validarUsuario(dto.getIdUsuario());

        log.info("Enviando notificacion a usuario id={}, correo={}",
                dto.getIdUsuario(), usuario.getCorreo());

        Notificacion notificacion = new Notificacion();

        notificacion.setIdUsuario(dto.getIdUsuario());
        notificacion.setDestinatario(usuario.getCorreo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());
        notificacion.setEstado("ENVIADA");

        Notificacion guardada = repository.save(notificacion);

        log.info("Notificacion creada exitosamente id={}", guardada.getId());

        return toDto(guardada);
    }

    public NotificacionDTO actualizarNotificacion(Long id, NotificacionUpdateDTO dto) {

        if (dto == null) {
            dto = new NotificacionUpdateDTO();
        }

        Notificacion notificacion = obtenerEntidadPorId(id);

        if (dto.getMensaje() != null && !dto.getMensaje().isBlank()) {
            notificacion.setMensaje(dto.getMensaje());
        }

        if (dto.getTipo() != null && !dto.getTipo().isBlank()) {
            notificacion.setTipo(dto.getTipo());
        }

        return toDto(repository.save(notificacion));
    }

    public NotificacionDTO marcarComoLeida(Long id) {

        Notificacion notificacion = obtenerEntidadPorId(id);

        if ("LEIDA".equalsIgnoreCase(notificacion.getEstado())) {

            throw new EstadoInvalidoException("La notificación ya está marcada como leída");
        }

        notificacion.setEstado("LEIDA");

        log.info("Notificacion id={} marcada como leida", id);

        return toDto(repository.save(notificacion));
    }

    public void eliminarNotificacion(Long id) {

        Notificacion notificacion = obtenerEntidadPorId(id);

        repository.delete(notificacion);

        log.info("Notificacion eliminada id={}", id);
    }

    private UsuarioDTO validarUsuario(Long idUsuario) {

        try {

            log.info("Consultando usuario id={}", idUsuario);

            UsuarioDTO usuario = usuarioClient.obtenerUsuario(idUsuario);

            log.info("Usuario encontrado: {}", usuario.getNombre());

            return usuario;

        } catch (FeignException.NotFound e) {

            log.warn("Usuario id={} no existe", idUsuario);

            throw new RecursoNoEncontradoException("Usuario no encontrado");

        } catch (FeignException e) {

            log.error("Error al consultar servicio Usuarios: {}", e.getMessage());

            throw new ServicioNoDisponibleException(
                    "Servicio de usuarios no disponible");
        }
    }

    private Notificacion obtenerEntidadPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Notificación no encontrada"));
    }

    private NotificacionDTO toDto(Notificacion n) {

        return new NotificacionDTO(
                n.getId(),
                n.getIdUsuario(),
                n.getDestinatario(),
                n.getMensaje(),
                n.getTipo(),
                n.getEstado()
        );
    }
}
