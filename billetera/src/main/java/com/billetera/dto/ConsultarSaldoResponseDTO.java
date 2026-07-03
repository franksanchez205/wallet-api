package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * DTO de respuesta con el saldo actual y el nombre del cliente.
 */
public class ConsultarSaldoResponseDTO {
    private BigDecimal saldo;
    private String nombre;
}
