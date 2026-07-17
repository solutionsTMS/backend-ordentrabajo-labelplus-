package com.etimet.orden_trabajo.domain.contratos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.etimet.orden_trabajo.domain.entities.OrdenesTrabajo;

public interface OrdenesTrabajoContrato {

    OrdenesTrabajo crearOrdenTrabajo(OrdenesTrabajo orden);

    Page<OrdenesTrabajo> obtenerOrdenesTrabajo(String buscar, Pageable pageable);

    OrdenesTrabajo obtenerOrdenTrabajoPorId(Long id);

    long contarOrdenesTrabajo();
}
