package com.billetera.repository;

import com.billetera.entity.TransaccionPendiente;
import com.billetera.enums.EstadoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
/**
 * Repositorio JPA para operaciones de persistencia de TransaccionPendiente.
 */
public interface TransaccionPendienteRepository extends JpaRepository<TransaccionPendiente, Long> {

    /**
     * Busca una transaccion pendiente por su sessionId.
     *
     * @param sessionId identificador unico de sesion
     * @return Optional con la transaccion encontrada
     */
    Optional<TransaccionPendiente> findBySessionId(String sessionId);

    /**
     * Busca transacciones en un estado especifico cuya expiracion
     * sea anterior a la fecha dada.
     *
     * @param estado estado de la transaccion
     * @param fecha  fecha limite de expiracion
     * @return lista de transacciones vencidas
     */
    List<TransaccionPendiente> findByEstadoAndExpiracionBefore(EstadoTransaccion estado, LocalDateTime fecha);
}
