package com.etimet.orden_trabajo.domain.records.ordenestrabajo;

import java.math.BigDecimal;

import com.etimet.orden_trabajo.domain.enums.ModalidadProducto;
import com.etimet.orden_trabajo.domain.enums.TipoTrabajo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrdenesTrabajoRequestRecord(
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        @NotNull(message = "La modalidad es obligatoria") ModalidadProducto modalidad,
        @NotNull(message = "El tipo de trabajo es obligatorio") TipoTrabajo tipoTrabajo,
        Long productoId,
        @NotNull(message = "La cantidad a fabricar es obligatoria") @Min(value = 1, message = "La cantidad mínima es 1") Integer cantidadFabricar,
        @NotBlank(message = "La observación es obligatoria") @Size(min = 3, max = 2000, message = "La observación debe tener entre 3 y 2000 caracteres") String observacion,
        BigDecimal avance,
        BigDecimal ancho,
        BigDecimal lyflat,
        @Size(max = 50) String formaEtiqueta,
        @NotNull(message = "El prepicado especial es obligatorio") Boolean prepicadoEspecial,
        @NotNull(message = "El corte superficial es obligatorio") Boolean corteSuperficial,
        @NotNull(message = "El indicador de impreso es obligatorio") Boolean esImpreso,
        @Size(max = 300) String arteAdjuntoNombre,
        @Size(max = 500) String acabados,
        @Size(max = 500) String colores) {
}
