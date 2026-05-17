package cl.duoc.backend_notificacion.dto;

import lombok.Data;

@Data
public class NotificacionCreateDTO {

    private Long idUsuario;
    private String mensaje;
    private String tipo;
}
