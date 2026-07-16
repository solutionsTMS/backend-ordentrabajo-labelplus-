package com.etimet.orden_trabajo.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Clientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCliente")
    private Long id;

    @Column(name = "CodigoCliente", nullable = false, length = 20, unique = true)
    private String codigoCliente;

    @Column(name = "IdentificacionCliente", nullable = false, length = 25, unique = true)
    private String identificacionCliente;

    @Column(name = "RazonSocial", nullable = false, length = 300)
    private String razonSocial;

    @Column(name = "NombreComercial", length = 300)
    private String nombreComercial;

    @Column(name = "Direccion", length = 300)
    private String direccion;

    @Column(name = "Zona", length = 150)
    private String zona;

    @Column(name = "Provincia", length = 150)
    private String provincia;

    @Column(name = "Ciudad", length = 150)
    private String ciudad;

    @Column(name = "Parroquia", length = 150)
    private String parroquia;

    @Column(name = "Telefono", length = 15)
    private String telefono;

    @Column(name = "Celular", length = 15)
    private String celular;

    @Column(name = "Email", length = 200)
    private String email;

    @Column(name = "Dias")
    private Integer dias;

    @Column(name = "Sexo", length = 1)
    private String sexo;

    @Column(name = "EstadoCivil", length = 1)
    private String estadoCivil;

    @Column(name = "NombreUsuario", length = 50)
    private String nombreUsuario;

    @Column(name = "EstadoCliente", nullable = false)
    private Boolean estadoCliente = Boolean.TRUE;

    @Column(name = "Calificacion")
    private Integer calificacion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    private void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
        normalizarTextos();
    }

    @PreUpdate
    private void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        normalizarTextos();
    }

    private void normalizarTextos() {
        if (this.razonSocial != null) {
            this.razonSocial = this.razonSocial.toUpperCase();
        }
        if (this.nombreComercial != null) {
            this.nombreComercial = this.nombreComercial.toUpperCase();
        }
        if (this.direccion != null) {
            this.direccion = this.direccion.toUpperCase();
        }
        if (this.zona != null) {
            this.zona = this.zona.toUpperCase();
        }
        if (this.provincia != null) {
            this.provincia = this.provincia.toUpperCase();
        }
        if (this.ciudad != null) {
            this.ciudad = this.ciudad.toUpperCase();
        }
        if (this.parroquia != null) {
            this.parroquia = this.parroquia.toUpperCase();
        }
    }

}
