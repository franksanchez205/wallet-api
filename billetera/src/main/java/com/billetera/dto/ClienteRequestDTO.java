package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO de solicitud para registrar un nuevo cliente.
 */
public class ClienteRequestDTO {
    private String documento;
    private String nombres;
    private String email;
    private String celular;
}
