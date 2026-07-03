package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO de solicitud para recargar saldo en la billetera.
 */
public class RecargaRequestDTO {

    private String documento;
    private String celular;
    private BigDecimal valor;
    
}
