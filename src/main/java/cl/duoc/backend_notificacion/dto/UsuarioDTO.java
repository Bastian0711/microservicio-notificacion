package cl.duoc.backend_notificacion.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long idUsuario;
    private String nombre;
    private String correo;

    public String getCorreo() {
        return correo;
    }
}
