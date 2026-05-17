package cl.duoc.backend_notificacion.repository;

import cl.duoc.backend_notificacion.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}