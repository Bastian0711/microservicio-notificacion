package cl.duoc.backend_notificacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private Long id;
    private Long idUsuario;
    private String destinatario;
    private String mensaje;
    private String tipo;
    private String estado;
}
