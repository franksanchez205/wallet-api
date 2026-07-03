package com.billetera.controller;

import com.billetera.dto.ClienteRequestDTO;
import com.billetera.dto.StandardResponse;
import com.billetera.entity.Cliente;
import com.billetera.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Registra un nuevo cliente y crea su billetera asociada con saldo 0.
     *
     * @param request datos del cliente (documento, nombres, email, celular)
     * @return cliente registrado
     */
    @PostMapping("/registrar")
    public ResponseEntity<StandardResponse<Cliente>> registrar(@RequestBody ClienteRequestDTO request) {
        try {

            Cliente cliente = clienteService.registrarCliente(request);
            log.info("Cliente registrado: {}", request.getDocumento());

            return ResponseEntity.ok(StandardResponse.success("Cliente registrado exitosamente", cliente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(StandardResponse.error("01", e.getMessage()));
        }
    }
}
