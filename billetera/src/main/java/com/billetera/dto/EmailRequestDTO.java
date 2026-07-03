package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO para representar una solicitud de envio de correo electronico.
 */
public class EmailRequestDTO {
    private String from;
    private String to;
    private String subject;
    private String text;
}
