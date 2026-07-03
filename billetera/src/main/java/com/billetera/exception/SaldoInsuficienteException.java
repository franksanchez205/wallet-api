package com.billetera.exception;

/**
 * Excepcion lanzada cuando la billetera no tiene saldo suficiente
 * para realizar un pago.
 */
public class SaldoInsuficienteException extends RuntimeException {

    /**
     * Crea una excepcion con mensaje descriptivo.
     *
     * @param message descripcion del error
     */
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
