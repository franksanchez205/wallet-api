package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO de solicitud para consultar el saldo de una billetera.
 */
public class ConsultarSaldoRequestDTO {
    private String documento;
    private String celular;
}
