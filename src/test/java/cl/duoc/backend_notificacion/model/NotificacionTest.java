package cl.duoc.backend_notificacion.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class NotificacionTest {

    @Test
    void testCrearNotificacionConDatosValidos() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setIdUsuario(10L);
        n.setDestinatario("juan@email.com");
        n.setMensaje("Tu pedido fue confirmado");
        n.setTipo("EMAIL");
        n.setEstado("ENVIADA");

        assertEquals(1L, n.getId());
        assertEquals(10L, n.getIdUsuario());
        assertEquals("juan@email.com", n.getDestinatario());
        assertEquals("Tu pedido fue confirmado", n.getMensaje());
        assertEquals("EMAIL", n.getTipo());
        assertEquals("ENVIADA", n.getEstado());
    }

    @Test
    void testNotificacionEstadoLeida() {
        Notificacion n = new Notificacion();
        n.setEstado("LEIDA");

        assertEquals("LEIDA", n.getEstado());
    }

    @Test
    void testNotificacionConstructorVacio() {
        Notificacion n = new Notificacion();

        assertNull(n.getId());
        assertNull(n.getMensaje());
        assertNull(n.getEstado());
    }
}