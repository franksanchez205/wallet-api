package com.billetera.service;

import com.billetera.dto.*;
import com.billetera.entity.Billetera;
import com.billetera.entity.TransaccionPendiente;
import com.billetera.enums.EstadoTransaccion;
import com.billetera.exception.SaldoInsuficienteException;
import com.billetera.repository.BilleteraRepository;
import com.billetera.repository.TransaccionPendienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
/**
 * Servicio que implementa la logica de negocio para las operaciones
 * de la billetera digital.
 */
public class BilleteraService {

    private final BilleteraRepository billeteraRepository;
    private final TransaccionPendienteRepository transaccionPendienteRepository;
    private final EmailService emailService;

    @Value("${app.transaccion.expiracion-minutos:3}")
    private int expiracionMinutos;

    /**
     * Consulta el saldo de una billetera por documento y celular del cliente.
     *
     * @param request documento y celular del cliente
     * @return saldo y nombre del cliente
     * @throws IllegalArgumentException si no se encuentra la billetera
     */
    public ConsultarSaldoResponseDTO consultarSaldo(ConsultarSaldoRequestDTO request) {

        Optional<Billetera> billetera = billeteraRepository
                .findByClienteDocumentoAndClienteCelular(request.getDocumento(), request.getCelular());

        if (billetera.isEmpty()) {
            throw new IllegalArgumentException("Billetera no encontrada para el cliente");

        }
        return ConsultarSaldoResponseDTO.builder()
                .saldo(billetera.get().getSaldo())
                .nombre(billetera.get().getCliente().getNombres())
                .build();

    }

    /**
     * Recarga saldo en la billetera de un cliente.
     * El valor debe ser mayor a 0.
     *
     * @param request documento, celular y valor a recargar
     * @return mensaje de confirmacion
     * @throws IllegalArgumentException si el valor es <= 0 o la billetera no existe
     */
    @Transactional
    public MessageResponseDTO recargarSaldo(RecargaRequestDTO request) {

        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("El valor de la recarga debe ser mayor a 0");
            throw new IllegalArgumentException("El valor de la recarga debe ser mayor a 0");
        }
        Billetera billetera = billeteraRepository
                .findByClienteDocumentoAndClienteCelular(request.getDocumento(), request.getCelular())
                .orElseThrow(() -> new IllegalArgumentException("Billetera no encontrada para el cliente"));

        billetera.setSaldo(billetera.getSaldo().add(request.getValor()));
        billetera = billeteraRepository.save(billetera);

        log.info("Recarga exitosa de {} para documento {}", request.getValor(), request.getDocumento());

        return MessageResponseDTO.builder()
                .message("Recarga exitosa de " + request.getValor() + " para documento " + request.getDocumento())
                .build();
    }

    /**
     * Inicia un proceso de pago. Verifica saldo suficiente, genera un
     * sessionId UUID y un token de 6 digitos, persiste la transaccion
     * como PENDIENTE y envia el token por email.
     *
     * @param request documento, celular y valor del pago
     * @return sessionId y mensaje informativo
     * @throws IllegalArgumentException si la billetera no existe
     * @throws SaldoInsuficienteException si el saldo es insuficiente
     */
    @Transactional
    public IniciarPagoResponseDTO iniciarPago(IniciarPagoRequestDTO request) {
        Billetera billetera = billeteraRepository
                .findByClienteDocumentoAndClienteCelular(request.getDocumento(), request.getCelular())
                .orElseThrow(() -> new IllegalArgumentException("Billetera no encontrada"));

        if (billetera.getSaldo().compareTo(request.getValor()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar el pago");
        }

        String sessionId = UUID.randomUUID().toString();
        String token = String.format("%06d", new Random().nextInt(999999));

        TransaccionPendiente transaccion = new TransaccionPendiente();
        transaccion.setSessionId(sessionId);
        transaccion.setToken(token);
        transaccion.setValor(request.getValor());
        transaccion.setCliente(billetera.getCliente());
        transaccion.setExpiracion(LocalDateTime.now().plusMinutes(expiracionMinutos));
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        transaccionPendienteRepository.save(transaccion);

        emailService.enviarToken(billetera.getCliente().getEmail(), token);

        log.info("Pago iniciado para documento {}: sessionId={}", request.getDocumento(), sessionId);

        return IniciarPagoResponseDTO.builder()
                .sessionId(sessionId)
                .mensaje("Token enviado al correo registrado")
                .build();
    }

    /**
     * Confirma un pago validando el token, el estado PENDIENTE y la fecha
     * de expiracion. Si es correcto, descuenta el saldo y marca la
     * transaccion como COMPLETADA.
     *
     * @param request sessionId y token de confirmacion
     * @return mensaje de confirmacion
     * @throws IllegalArgumentException si la transaccion no existe,
     *         ya fue procesada, el token expiro o es incorrecto
     */
    @Transactional
    public MessageResponseDTO confirmarPago(ConfirmarPagoRequestDTO request) {
        TransaccionPendiente transaccion = transaccionPendienteRepository
                .findBySessionId(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaccion no encontrada"));

        if (transaccion.getEstado() != EstadoTransaccion.PENDIENTE) {
            throw new IllegalArgumentException("La transaccion ya fue procesada o expiro");
        }

        if (transaccion.getExpiracion().isBefore(LocalDateTime.now())) {
            transaccion.setEstado(EstadoTransaccion.EXPIRADA);
            transaccionPendienteRepository.save(transaccion);
            throw new IllegalArgumentException("El token ha expirado");
        }

        if (!transaccion.getToken().equals(request.getToken())) {
            throw new IllegalArgumentException("Token incorrecto");
        }

        Billetera billetera = billeteraRepository
                .findByCliente(transaccion.getCliente())
                .orElseThrow(() -> new IllegalArgumentException("Billetera no encontrada"));

        billetera.setSaldo(billetera.getSaldo().subtract(transaccion.getValor()));
        billeteraRepository.save(billetera);

        transaccion.setEstado(EstadoTransaccion.COMPLETADA);
        transaccionPendienteRepository.save(transaccion);

        log.info("Pago confirmado para sessionId={}", request.getSessionId());
        return MessageResponseDTO.builder()
                .message("Pago confirmado exitosamente")
                .build();

    }

    /**
     * Expira las transacciones pendientes cuyo tiempo de expiracion haya vencido.
     * Se ejecuta cada 5 minutos mediante el scheduler.
     */
    @Transactional
    public void expirarTransaccionesVencidas() {
        LocalDateTime ahora = LocalDateTime.now();
        java.util.List<TransaccionPendiente> vencidas = transaccionPendienteRepository
                .findByEstadoAndExpiracionBefore(EstadoTransaccion.PENDIENTE, ahora);

        for (TransaccionPendiente t : vencidas) {
            t.setEstado(EstadoTransaccion.EXPIRADA);
            transaccionPendienteRepository.save(t);
            log.info("Transaccion {} expirada por scheduler", t.getSessionId());
        }

        if (!vencidas.isEmpty()) {
            log.info("Scheduler: {} transacciones expiradas correctamente", vencidas.size());
        }
    }
}
