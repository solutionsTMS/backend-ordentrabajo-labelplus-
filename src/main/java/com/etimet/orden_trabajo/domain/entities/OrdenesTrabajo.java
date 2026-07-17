package com.etimet.orden_trabajo.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.etimet.orden_trabajo.domain.enums.EstadoOrdenTrabajo;
import com.etimet.orden_trabajo.domain.enums.ModalidadProducto;
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
@Table(name = "ordenes_trabajo")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenesTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdOrdenTrabajo")
    private Long id;

    @Column(name = "Codigo", nullable = false, length = 30, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IdCliente", nullable = false)
    private Clientes cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "Modalidad", nullable = false, length = 20)
    private ModalidadProducto modalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "TipoTrabajo", nullable = false, length = 30)
    private TipoTrabajo tipoTrabajo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdProductoTerminado")
    private ProductosTerminados producto;

    @Column(name = "CantidadFabricar", nullable = false)
    private Integer cantidadFabricar;

    @Column(name = "Observacion", nullable = false, length = 2000)
    private String observacion;

    @Column(name = "Avance", precision = 12, scale = 2)
    private BigDecimal avance;

    @Column(name = "Ancho", precision = 12, scale = 2)
    private BigDecimal ancho;

    @Column(name = "Lyflat", precision = 12, scale = 2)
    private BigDecimal lyflat;

    @Column(name = "FormaEtiqueta", length = 50)
    private String formaEtiqueta;

    @Column(name = "PrepicadoEspecial", nullable = false)
    private Boolean prepicadoEspecial = Boolean.FALSE;

    @Column(name = "CorteSuperficial", nullable = false)
    private Boolean corteSuperficial = Boolean.FALSE;

    @Column(name = "EsImpreso", nullable = false)
    private Boolean esImpreso = Boolean.FALSE;

    @Column(name = "ArteAdjuntoNombre", length = 300)
    private String arteAdjuntoNombre;

    @Column(name = "Acabados", length = 500)
    private String acabados;

    @Column(name = "Colores", length = 500)
    private String colores;

    @Column(name = "DatosTecnicos", length = 2000)
    private String datosTecnicos;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false, length = 20)
    private EstadoOrdenTrabajo estado = EstadoOrdenTrabajo.Pendiente;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    private void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
        if (estado == null) {
            estado = EstadoOrdenTrabajo.Pendiente;
        }
        if (prepicadoEspecial == null) {
            prepicadoEspecial = Boolean.FALSE;
        }
        if (corteSuperficial == null) {
            corteSuperficial = Boolean.FALSE;
        }
        if (esImpreso == null) {
            esImpreso = Boolean.FALSE;
        }
        normalizarTextos();
    }

    @PreUpdate
    private void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        normalizarTextos();
    }

    private void normalizarTextos() {
        if (this.codigo != null) {
            this.codigo = this.codigo.toUpperCase();
        }
        if (this.formaEtiqueta != null) {
            this.formaEtiqueta = this.formaEtiqueta.toUpperCase();
        }
        if (this.arteAdjuntoNombre != null) {
            this.arteAdjuntoNombre = this.arteAdjuntoNombre.trim();
        }
    }
}
