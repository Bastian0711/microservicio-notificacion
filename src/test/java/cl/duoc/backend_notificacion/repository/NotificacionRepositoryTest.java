package cl.duoc.backend_notificacion.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import cl.duoc.backend_notificacion.model.Notificacion;

@DataJpaTest
class NotificacionRepositoryTest {

    @Autowired
    private NotificacionRepository repository;

    @Test
    void testGuardarNotificacion() {
        Notificacion n = new Notificacion();
        n.setIdUsuario(1L);
        n.setDestinatario("juan@email.com");
        n.setMensaje("Tu pedido fue confirmado");
        n.setTipo("EMAIL");
        n.setEstado("ENVIADA");

        Notificacion guardada = repository.save(n);

        assertNotNull(guardada.getId());
        assertEquals("juan@email.com", guardada.getDestinatario());
    }

    @Test
    void testBuscarPorId() {
        Notificacion n = new Notificacion();
        n.setIdUsuario(2L);
        n.setDestinatario("maria@email.com");
        n.setMensaje("Mensaje de prueba");
        n.setTipo("SMS");
        n.setEstado("ENVIADA");

        Notificacion guardada = repository.save(n);
        Optional<Notificacion> encontrada = repository.findById(guardada.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("maria@email.com", encontrada.get().getDestinatario());
    }

    @Test
    void testListarTodos() {
        Notificacion n1 = new Notificacion();
        n1.setIdUsuario(1L);
        n1.setDestinatario("a@email.com");
        n1.setMensaje("msg1");
        n1.setTipo("EMAIL");
        n1.setEstado("ENVIADA");

        Notificacion n2 = new Notificacion();
        n2.setIdUsuario(2L);
        n2.setDestinatario("b@email.com");
        n2.setMensaje("msg2");
        n2.setTipo("SMS");
        n2.setEstado("LEIDA");

        repository.save(n1);
        repository.save(n2);

        List<Notificacion> lista = repository.findAll();

        assertTrue(lista.size() >= 2);
    }
}