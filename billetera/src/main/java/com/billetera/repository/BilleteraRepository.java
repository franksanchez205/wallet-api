package com.billetera.repository;

import com.billetera.entity.Billetera;
import com.billetera.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/**
 * Repositorio JPA para operaciones de persistencia de Billetera.
 */
public interface BilleteraRepository extends JpaRepository<Billetera, Long> {

    /**
     * Busca una billetera por su cliente asociado.
     *
     * @param cliente entidad Cliente
     * @return Optional con la billetera encontrada
     */
    Optional<Billetera> findByCliente(Cliente cliente);

    /**
     * Busca una billetera por el documento y celular del cliente.
     *
     * @param documento numero de documento
     * @param celular   numero de celular
     * @return Optional con la billetera encontrada
     */
    Optional<Billetera> findByClienteDocumentoAndClienteCelular(String documento, String celular);
}
