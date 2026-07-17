package com.etimet.orden_trabajo.domain.records.productosterminados;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.etimet.orden_trabajo.domain.enums.TipoTrabajo;

public record ProductosTerminadosResponseRecord(
        Long id,
        String codigo,
        String nombre,
        Long clienteId,
        TipoTrabajo tipoTrabajo,
        String datosTecnicos,
        BigDecimal avance,
        BigDecimal ancho,
        BigDecimal lyflat,
        String formaEtiqueta,
        Boolean estadoProductoTerminado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {
}
