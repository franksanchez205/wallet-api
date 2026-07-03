package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO de solicitud para iniciar un proceso de pago.
 */
public class IniciarPagoRequestDTO {
    private String documento;
    private String celular;
    private BigDecimal valor;
}
