package cl.duoc.backend_notificacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionUpdateDTO {
    private String mensaje;
    private String tipo;
}
