package com.etimet.orden_trabajo.domain.records.productosterminados;

import java.math.BigDecimal;

import com.etimet.orden_trabajo.domain.enums.TipoTrabajo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductosTerminadosRequestRecord(
        @NotBlank(message = "El código del producto es obligatorio") @Size(max = 30) String codigo,
        @NotBlank(message = "El nombre del producto es obligatorio") @Size(max = 300) String nombre,
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        @NotNull(message = "El tipo de trabajo es obligatorio") TipoTrabajo tipoTrabajo,
        @NotBlank(message = "Los datos técnicos son obligatorios") @Size(max = 2000) String datosTecnicos,
        BigDecimal avance,
        BigDecimal ancho,
        BigDecimal lyflat,
        @Size(max = 50) String formaEtiqueta) {
}
