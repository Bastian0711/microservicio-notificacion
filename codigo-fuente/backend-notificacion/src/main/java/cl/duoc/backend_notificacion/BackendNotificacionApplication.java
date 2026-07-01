package cl.duoc.backend_notificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BackendNotificacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendNotificacionApplication.class, args);
    }
}