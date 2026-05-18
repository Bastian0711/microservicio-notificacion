package cl.duoc.backend_notificacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.backend_notificacion.client.UsuarioClient;
import cl.duoc.backend_notificacion.dto.NotificacionCreateDTO;
import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import cl.duoc.backend_notificacion.dto.UsuarioDTO;
import cl.duoc.backend_notificacion.exception.RecursoNoEncontradoException;
import cl.duoc.backend_notificacion.exception.ServicioNoDisponibleException;
import cl.duoc.backend_notificacion.model.Notificacion;
import cl.duoc.backend_notificacion.repository.NotificacionRepository;
import feign.FeignException;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioClient usuarioClient;

    public NotificacionService(NotificacionRepository notificacionRepository,
            UsuarioClient usuarioClient) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioClient = usuarioClient;
    }

    public List<Notificacion> listarNotificaciones() {
        return notificacionRepository.findAll();
    }

    public Notificacion guardarNotificacion(Notificacion notificacion) {
        notificacion.setEstado("PENDIENTE");

        return notificacionRepository.save(notificacion);
    }

    public Notificacion buscarPorId(Long id) {
        return notificacionRepository.findById(id).orElse(null);
    }

    public Notificacion actualizarNotificacion(Long id, Notificacion notificacionActualizada) {
        Notificacion notificacion = buscarPorId(id);

        if (notificacion == null) {
            return null;
        }

        notificacion.setDestinatario(notificacionActualizada.getDestinatario());
        notificacion.setMensaje(notificacionActualizada.getMensaje());
        notificacion.setTipo(notificacionActualizada.getTipo());
        notificacion.setEstado(notificacionActualizada.getEstado());

        return notificacionRepository.save(notificacion);
    }

    public Notificacion marcarComoLeida(Long id) {
        Notificacion notificacion = buscarPorId(id);

        if (notificacion == null) {
            return null;
        }

        notificacion.setEstado("LEIDA");

        return notificacionRepository.save(notificacion);
    }

    public boolean eliminarNotificacion(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada"));

        notificacionRepository.delete(notificacion);
        return true;
    }

    public NotificacionDTO enviarNotificacion(NotificacionCreateDTO dto) {

        UsuarioDTO usuario = validarUsuario(dto.getIdUsuario());

        Notificacion notificacion = new Notificacion();
        notificacion.setDestinatario(usuario.getCorreo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());
        notificacion.setEstado("ENVIADA");

        notificacionRepository.save(notificacion);

        NotificacionDTO response = new NotificacionDTO();
        response.setId(notificacion.getId());
        response.setIdUsuario(dto.getIdUsuario());
        response.setDestinatario(notificacion.getDestinatario());
        response.setMensaje(notificacion.getMensaje());
        response.setTipo(notificacion.getTipo());
        response.setEstado(notificacion.getEstado());

        return response;
    }

    private UsuarioDTO validarUsuario(Long idUsuario) {
        try {
            UsuarioDTO usuario = usuarioClient.obtenerUsuario(idUsuario);

            if (usuario == null) {
                throw new RecursoNoEncontradoException("Usuario no encontrado");
            }

            return usuario;

        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        } catch (FeignException e) {
            throw new ServicioNoDisponibleException("No se pudo consultar el microservicio de usuarios");
        }
    }

}
