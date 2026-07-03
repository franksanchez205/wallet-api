package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * DTO de respuesta al iniciar un pago, contiene el sessionId
 * necesario para confirmar la transaccion.
 */
public class IniciarPagoResponseDTO {
    private String sessionId;
    private String mensaje;
}
