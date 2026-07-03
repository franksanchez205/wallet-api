package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * DTO generico para respuestas que solo contienen un mensaje de texto.
 */
public class MessageResponseDTO {
    private String message;
}
