package com.etimet.orden_trabajo.domain.records.ordenestrabajo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.etimet.orden_trabajo.domain.enums.EstadoOrdenTrabajo;
import com.etimet.orden_trabajo.domain.enums.ModalidadProducto;
import com.etimet.orden_trabajo.domain.enums.TipoTrabajo;

public record OrdenesTrabajoResponseRecord(
        Long id,
        String codigo,
        Long clienteId,
        String clienteNombre,
        ModalidadProducto modalidad,
        TipoTrabajo tipoTrabajo,
        Long productoId,
        String productoCodigo,
        String productoNombre,
        Integer cantidadFabricar,
        String observacion,
        BigDecimal avance,
        BigDecimal ancho,
        BigDecimal lyflat,
        String formaEtiqueta,
        Boolean prepicadoEspecial,
        Boolean corteSuperficial,
        Boolean esImpreso,
        String arteAdjuntoNombre,
        String acabados,
        String colores,
        String datosTecnicos,
        EstadoOrdenTrabajo estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {
}
