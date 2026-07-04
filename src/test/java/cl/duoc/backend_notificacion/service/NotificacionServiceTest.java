package cl.duoc.backend_notificacion.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.backend_notificacion.client.UsuarioClient;
import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import cl.duoc.backend_notificacion.dto.NotificacionUpdateDTO;
import cl.duoc.backend_notificacion.exception.EstadoInvalidoException;
import cl.duoc.backend_notificacion.exception.RecursoNoEncontradoException;
import cl.duoc.backend_notificacion.model.Notificacion;
import cl.duoc.backend_notificacion.repository.NotificacionRepository;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void testListarNotificaciones() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setIdUsuario(1L);
        n.setDestinatario("juan@email.com");
        n.setMensaje("Hola");
        n.setTipo("EMAIL");
        n.setEstado("ENVIADA");

        when(repository.findAll()).thenReturn(List.of(n));

        List<NotificacionDTO> resultado = notificacionService.listarNotificaciones();

        assertEquals(1, resultado.size());
        assertEquals("ENVIADA", resultado.get(0).getEstado());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testObtenerPorIdNoExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> notificacionService.obtenerPorId(99L));
    }

    @Test
    void testMarcarComoLeidaYaLeida() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setEstado("LEIDA");
        n.setIdUsuario(1L);
        n.setDestinatario("a@b.com");
        n.setMensaje("msg");
        n.setTipo("EMAIL");

        when(repository.findById(1L)).thenReturn(Optional.of(n));

        assertThrows(EstadoInvalidoException.class,
                () -> notificacionService.marcarComoLeida(1L));
    }

    @Test
    void testMarcarComoLeidaExitoso() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setEstado("ENVIADA");
        n.setIdUsuario(1L);
        n.setDestinatario("a@b.com");
        n.setMensaje("msg");
        n.setTipo("EMAIL");

        when(repository.findById(1L)).thenReturn(Optional.of(n));
        when(repository.save(any(Notificacion.class))).thenReturn(n);

        NotificacionDTO resultado = notificacionService.marcarComoLeida(1L);

        assertEquals("LEIDA", resultado.getEstado());
    }

    @Test
    void testEliminarNoExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> notificacionService.eliminarNotificacion(99L));
    }

    @Test
    void testActualizarNotificacion() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setEstado("ENVIADA");
        n.setIdUsuario(1L);
        n.setDestinatario("a@b.com");
        n.setMensaje("mensaje original");
        n.setTipo("EMAIL");

        NotificacionUpdateDTO dto = new NotificacionUpdateDTO();
        dto.setMensaje("mensaje actualizado");

        when(repository.findById(1L)).thenReturn(Optional.of(n));
        when(repository.save(any(Notificacion.class))).thenReturn(n);

        NotificacionDTO resultado = notificacionService.actualizarNotificacion(1L, dto);

        assertNotNull(resultado);
    }
}