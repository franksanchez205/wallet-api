package com.billetera.exception;

import com.billetera.dto.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
/**
 * Manejador global de excepciones que captura errores de todos
 * los controladores y retorna respuestas estandarizadas.
 */
public class GlobalExceptionHandler {

    /**
     * Maneja SaldoInsuficienteException retornando HTTP 400 con codigo "03".
     *
     * @param ex excepcion capturada
     * @return respuesta con error "03"
     */
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<StandardResponse<Void>> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StandardResponse.error("03", ex.getMessage()));
    }

    /**
     * Maneja IllegalArgumentException retornando HTTP 400 con codigo "01".
     *
     * @param ex excepcion capturada
     * @return respuesta con error "01"
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StandardResponse.error("01", ex.getMessage()));
    }

    /**
     * Maneja cualquier excepcion no contemplada retornando HTTP 500 con codigo "99".
     *
     * @param ex excepcion capturada
     * @return respuesta con error "99"
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Void>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardResponse.error("99", "Error interno del servidor"));
    }
}
