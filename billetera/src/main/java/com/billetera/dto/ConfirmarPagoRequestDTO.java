package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO de solicitud para confirmar un pago con el token recibido por email.
 */
public class ConfirmarPagoRequestDTO {
    private String sessionId;
    private String token;
}
