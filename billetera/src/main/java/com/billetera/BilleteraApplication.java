package com.billetera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BilleteraApplication {

    /**
     * Método de entrada que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(BilleteraApplication.class, args);
    }
}
