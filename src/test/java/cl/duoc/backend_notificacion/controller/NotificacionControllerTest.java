package cl.duoc.backend_notificacion.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.backend_notificacion.dto.NotificacionDTO;
import cl.duoc.backend_notificacion.service.NotificacionService;

class NotificacionControllerTest {

    private final NotificacionService service = mock(NotificacionService.class);
    private final NotificacionController controller = new NotificacionController(service);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    @Test
    void testListarRetorna200() throws Exception {
        when(service.listarNotificaciones()).thenReturn(List.of());

        mockMvc.perform(get("/api/v2/notificaciones"))
                .andExpect(status().isOk());
    }

    @Test
    void testBuscarPorIdRetorna200() throws Exception {
        NotificacionDTO dto = new NotificacionDTO(1L, 1L, "a@b.com", "msg", "EMAIL", "ENVIADA");

        when(service.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v2/notificaciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarRetorna200() throws Exception {
        doNothing().when(service).eliminarNotificacion(1L);

        mockMvc.perform(delete("/api/v2/notificaciones/1"))
                .andExpect(status().isOk());
    }
}