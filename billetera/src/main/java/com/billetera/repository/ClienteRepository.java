package com.billetera.repository;

import com.billetera.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/**
 * Repositorio JPA para operaciones de persistencia de Cliente.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su numero de documento.
     *
     * @param documento numero de documento
     * @return Optional con el cliente encontrado
     */
    Optional<Cliente> findByDocumento(String documento);

    /**
     * Busca un cliente por documento y celular.
     *
     * @param documento numero de documento
     * @param celular   numero de celular
     * @return Optional con el cliente encontrado
     */
    Optional<Cliente> findByDocumentoAndCelular(String documento, String celular);

    /**
     * Verifica si existe un cliente con el documento dado.
     *
     * @param documento numero de documento
     * @return true si existe, false en caso contrario
     */
    boolean existsByDocumento(String documento);

    /**
     * Verifica si existe un cliente con el email dado.
     *
     * @param email correo electronico
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un cliente con el celular dado.
     *
     * @param celular numero de celular
     * @return true si existe, false en caso contrario
     */
    boolean existsByCelular(String celular);
}
