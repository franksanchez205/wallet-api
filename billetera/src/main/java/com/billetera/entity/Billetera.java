package com.billetera.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "billeteras")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad que representa la billetera digital asociada a un cliente.
 * Cada cliente tiene exactamente una billetera.
 */
public class Billetera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;
}
