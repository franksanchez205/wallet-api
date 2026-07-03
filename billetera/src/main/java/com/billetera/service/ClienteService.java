package com.billetera.service;

import com.billetera.entity.Cliente;
import com.billetera.dto.ClienteRequestDTO;
import com.billetera.entity.Billetera;
import com.billetera.repository.ClienteRepository;
import com.billetera.repository.BilleteraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
/**
 * Servicio que implementa la logica de negocio para el registro
 * y consulta de clientes.
 */
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BilleteraRepository billeteraRepository;

    /**
     * Registra un nuevo cliente y crea su billetera asociada con saldo 0.
     * Valida que no exista ya un cliente con el mismo documento y celular.
     *
     * @param request datos del cliente
     * @return cliente persistido
     * @throws IllegalArgumentException si el cliente ya esta registrado
     */
    @Transactional
    public Cliente registrarCliente(ClienteRequestDTO request) {

        Optional<Cliente> clienteopc = clienteRepository.findByDocumentoAndCelular(request.getDocumento(),request.getCelular());

        if (clienteopc.isPresent()) {
            throw new IllegalArgumentException("El cliente ya esta registrado con ese documento y celular");
        }

        Cliente cliente = new Cliente();

        cliente.setDocumento(request.getDocumento());
        cliente.setNombres(request.getNombres());
        cliente.setEmail(request.getEmail());
        cliente.setCelular(request.getCelular());
        
        clienteRepository.save(cliente);

        Billetera billetera = new Billetera();
        billetera.setCliente(cliente);
        billetera.setSaldo(BigDecimal.ZERO);
        
        billeteraRepository.save(billetera);

        log.info("Cliente registrado exitosamente: {}", request.getDocumento());
        return cliente;
    }

    /**
     * Busca un cliente por su numero de documento.
     *
     * @param documento numero de documento
     * @return Optional con el cliente encontrado
     */
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento);
    }

    /**
     * Busca un cliente por documento y celular.
     *
     * @param documento numero de documento
     * @param celular   numero de celular
     * @return Optional con el cliente encontrado
     */
    public Optional<Cliente> buscarPorDocumentoYCelular(String documento, String celular) {
        return clienteRepository.findByDocumentoAndCelular(documento, celular);
    }
}
