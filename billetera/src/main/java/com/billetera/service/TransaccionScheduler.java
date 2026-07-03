package com.billetera.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
/**
 * Componente programado que ejecuta la expiracion automatica
 * de transacciones pendientes vencidas.
 */
public class TransaccionScheduler {

    private final BilleteraService billeteraService;

    /**
     * Expira las transacciones pendientes cuyo tiempo de expiracion haya vencido.
     * Se ejecuta cada 5 minutos.
     */
    @Scheduled(cron = "${app.cron.expiracion-transacciones}", zone = "America/Bogota")
    public void expirarTransaccionesVencidas() {
        try {
            log.info("Iniciando verificacion de transacciones vencidas: {}", java.time.LocalDateTime.now());
            billeteraService.expirarTransaccionesVencidas();
            log.info("Verificacion de transacciones vencidas finalizada");
        } catch (Exception e) {
            log.error("Error al expirar transacciones vencidas: {}", e.getMessage(), e);
        }
    }
}
