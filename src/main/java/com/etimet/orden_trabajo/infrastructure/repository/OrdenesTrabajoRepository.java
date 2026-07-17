package com.etimet.orden_trabajo.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.etimet.orden_trabajo.domain.contratos.OrdenesTrabajoContrato;
import com.etimet.orden_trabajo.domain.entities.OrdenesTrabajo;
import com.etimet.orden_trabajo.exceptions.RegistroNoEncontradoException;
import com.etimet.orden_trabajo.infrastructure.repository.jpa.OrdenesTrabajoJpa;
import com.etimet.orden_trabajo.infrastructure.specifications.OrdenesTrabajoSpecification;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrdenesTrabajoRepository implements OrdenesTrabajoContrato {

    private final OrdenesTrabajoJpa ordenesTrabajoJpa;

    @Override
    public OrdenesTrabajo crearOrdenTrabajo(OrdenesTrabajo orden) {
        return ordenesTrabajoJpa.save(orden);
    }

    @Override
    public Page<OrdenesTrabajo> obtenerOrdenesTrabajo(String buscar, Pageable pageable) {
        List<String> columnasABuscar = List.of(
                "codigo",
                "estado",
                "cliente.razonSocial",
                "cliente.codigoCliente",
                "producto.codigo",
                "producto.nombre");
        return ordenesTrabajoJpa.findAll(
                Specification.where(OrdenesTrabajoSpecification.busquedaGlobal(buscar, columnasABuscar)),
                pageable);
    }

    @Override
    public OrdenesTrabajo obtenerOrdenTrabajoPorId(Long id) {
        return ordenesTrabajoJpa.findById(id).orElseThrow(RegistroNoEncontradoException::new);
    }

    @Override
    public long contarOrdenesTrabajo() {
        return ordenesTrabajoJpa.count();
    }
}
