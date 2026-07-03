package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO de respuesta de la API de Mailgun al enviar un correo.
 */
public class EmailResponseDTO {
    private String id;
    private String message;
}
