package cl.duoc.backend_notificacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.backend_notificacion.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByIdUsuario(Long idUsuario);
    List<Notificacion> findByEstado(String estado);
}
