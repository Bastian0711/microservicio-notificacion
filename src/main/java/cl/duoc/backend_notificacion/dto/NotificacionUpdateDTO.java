package cl.duoc.backend_notificacion.dto;

import lombok.Data;

@Data
public class NotificacionUpdateDTO {

    private String mensaje;
    private String tipo;
    private String estado;
}
