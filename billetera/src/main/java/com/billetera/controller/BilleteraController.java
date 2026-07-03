package com.billetera.controller;

import com.billetera.dto.*;
import com.billetera.entity.Billetera;
import com.billetera.service.BilleteraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/billetera")
@RequiredArgsConstructor
public class BilleteraController {

    private final BilleteraService billeteraService;

    /**
     * Consulta el saldo de una billetera por documento y celular del cliente.
     *
     * @param request documento y celular del cliente
     * @return saldo actual y nombre del cliente
     */
    @GetMapping
    public ResponseEntity<StandardResponse<ConsultarSaldoResponseDTO>> consultarSaldos(
            @RequestBody ConsultarSaldoRequestDTO request) {

        ConsultarSaldoResponseDTO billetera = billeteraService.consultarSaldo(request);

        if (billetera == null) {

            return ResponseEntity.badRequest()
                    .body(StandardResponse.error("02", "Billetera no encontrada"));
        }

        return ResponseEntity.ok(StandardResponse.success(billetera));
    }

    /**
     * Recarga saldo en la billetera de un cliente.
     *
     * @param request documento, celular y valor a recargar
     * @return mensaje de confirmacion de recarga
     */
    @PostMapping("/recargar")
    public ResponseEntity<StandardResponse<MessageResponseDTO>> recargar(
            @RequestBody RecargaRequestDTO request) {
        try {
            
            billeteraService.recargarSaldo(request);


            return ResponseEntity.ok(StandardResponse.success(new MessageResponseDTO("Recarga Exitosa")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(StandardResponse.error("02", e.getMessage()));
        }
    }

    /**
     * Inicia un proceso de pago generando un token que se envia por email.
     *
     * @param request documento, celular y valor del pago
     * @return sessionId para confirmar el pago posteriormente
     */
    @PostMapping("/pago")
    public ResponseEntity<StandardResponse<IniciarPagoResponseDTO>> iniciarPago(
            @RequestBody IniciarPagoRequestDTO request) {
        try {
            IniciarPagoResponseDTO response = billeteraService.iniciarPago(request);
            return ResponseEntity.ok(StandardResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(StandardResponse.error("04", e.getMessage()));
        }
    }

    /**
     * Confirma un pago previamente iniciado usando el token enviado por email.
     *
     * @param request sessionId y token de confirmacion
     * @return mensaje de confirmacion del pago
     */
    @PostMapping("/confirmar")
    public ResponseEntity<StandardResponse<MessageResponseDTO>> confirmarPago(
            @RequestBody ConfirmarPagoRequestDTO request) {
        try {

            MessageResponseDTO mensaje = billeteraService.confirmarPago(request);
            return ResponseEntity.ok(StandardResponse.success(mensaje));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(StandardResponse.error("04", e.getMessage()));
        }
    }
}
