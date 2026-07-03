package com.billetera.entity;

import com.billetera.enums.EstadoTransaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones_pendientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad que representa una transaccion de pago pendiente de confirmacion,
 * con token de seguridad y tiempo de expiracion.
 */
public class TransaccionPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sessionId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado = EstadoTransaccion.PENDIENTE;
}
