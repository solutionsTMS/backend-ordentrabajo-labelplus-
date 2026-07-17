package com.etimet.orden_trabajo.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.etimet.orden_trabajo.domain.enums.TipoTrabajo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos_terminados")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductosTerminados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProductoTerminado")
    private Long id;

    @Column(name = "Codigo", nullable = false, length = 30, unique = true)
    private String codigo;

    @Column(name = "Nombre", nullable = false, length = 300)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IdCliente", nullable = false)
    private Clientes cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "TipoTrabajo", nullable = false, length = 30)
    private TipoTrabajo tipoTrabajo;

    @Column(name = "DatosTecnicos", nullable = false, length = 2000)
    private String datosTecnicos;

    @Column(name = "Avance", precision = 12, scale = 2)
    private BigDecimal avance;

    @Column(name = "Ancho", precision = 12, scale = 2)
    private BigDecimal ancho;

    @Column(name = "Lyflat", precision = 12, scale = 2)
    private BigDecimal lyflat;

    @Column(name = "FormaEtiqueta", length = 50)
    private String formaEtiqueta;

    @Column(name = "EstadoProductoTerminado", nullable = false)
    private Boolean estadoProductoTerminado = Boolean.TRUE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    private void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
        if (estadoProductoTerminado == null) {
            estadoProductoTerminado = Boolean.TRUE;
        }
        normalizarTextos();
    }

    @PreUpdate
    private void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        normalizarTextos();
    }

    private void normalizarTextos() {
        if (this.nombre != null) {
            this.nombre = this.nombre.toUpperCase();
        }
        if (this.codigo != null) {
            this.codigo = this.codigo.toUpperCase();
        }
        if (this.formaEtiqueta != null) {
            this.formaEtiqueta = this.formaEtiqueta.toUpperCase();
        }
    }
}
