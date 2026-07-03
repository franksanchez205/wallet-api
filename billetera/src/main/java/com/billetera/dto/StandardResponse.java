package com.billetera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO generico que encapsula todas las respuestas de la API
 * con indicador de exito, codigo de error, mensaje y datos.
 *
 * @param <T> tipo de los datos retornados
 */
public class StandardResponse<T> {
    private boolean success;
    private String codError;
    private String messageError;
    private T data;

    /**
     * Crea una respuesta exitosa con codigo "00" y mensaje por defecto.
     *
     * @param <T>  tipo de datos
     * @param data datos a incluir en la respuesta
     * @return respuesta con success=true, codError="00"
     */
    public static <T> StandardResponse<T> success(T data) {
        return new StandardResponse<>(true, "00", "Operacion exitosa", data);
    }

    /**
     * Crea una respuesta exitosa con mensaje personalizado.
     *
     * @param <T>     tipo de datos
     * @param message mensaje descriptivo
     * @param data    datos a incluir
     * @return respuesta con success=true, codError="00"
     */
    public static <T> StandardResponse<T> success(String message, T data) {
        return new StandardResponse<>(true, "00", message, data);
    }

    /**
     * Crea respuesta de cliente no encontrado.
     * BUG: success=true deberia ser false.
     *
     * @param <T>  tipo de datos
     * @param data datos adicionales
     * @return respuesta con success=true, codError="02"
     */
    public static <T> StandardResponse<T> clienteNoEncontrado(T data) {
        return new StandardResponse<>(true, "02", "Cliente no encontrado", data);

    }

    /**
     * Crea respuesta de token invalido o expirado.
     * BUG: success=true deberia ser false.
     *
     * @param <T>  tipo de datos
     * @param data datos adicionales
     * @return respuesta con success=true, codError="04"
     */
    public static <T> StandardResponse<T> tokenInvalido(T data) {
        return new StandardResponse<>(true, "04", "Token inválido o expirado", data);
    }

    /**
     * Crea una respuesta de error con codigo y mensaje personalizados.
     *
     * @param <T>      tipo de datos
     * @param codError codigo de error
     * @param message  mensaje descriptivo
     * @return respuesta con success=false, data=null
     */
    public static <T> StandardResponse<T> error(String codError, String message) {
        return new StandardResponse<>(false, codError, message, null);
    }
}
