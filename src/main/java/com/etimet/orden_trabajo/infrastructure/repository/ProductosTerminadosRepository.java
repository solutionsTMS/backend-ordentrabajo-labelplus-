package com.etimet.orden_trabajo.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.etimet.orden_trabajo.domain.contratos.ProductosTerminadosContrato;
import com.etimet.orden_trabajo.domain.entities.ProductosTerminados;
import com.etimet.orden_trabajo.exceptions.RegistroNoEncontradoException;
import com.etimet.orden_trabajo.infrastructure.repository.jpa.ProductosTerminadosJpa;
import com.etimet.orden_trabajo.infrastructure.specifications.ProductosTerminadosSpecification;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductosTerminadosRepository implements ProductosTerminadosContrato {

    private final ProductosTerminadosJpa productosTerminadosJpa;

    @Override
    public ProductosTerminados crearProductoTerminado(ProductosTerminados producto) {
        return productosTerminadosJpa.save(producto);
    }

    @Override
    public Page<ProductosTerminados> obtenerProductosTerminados(String buscar, Pageable pageable) {
        List<String> columnasABuscar = List.of("codigo", "nombre", "datosTecnicos", "formaEtiqueta",
                "cliente.razonSocial", "cliente.codigoCliente");
        return productosTerminadosJpa.findAll(Specification
                .where(ProductosTerminadosSpecification.busquedaGlobal(buscar, columnasABuscar))
                .and(ProductosTerminadosSpecification.activos()), pageable);
    }

    @Override
    public ProductosTerminados obtenerProductoTerminadoPorId(Long id) {
        return productosTerminadosJpa.findById(id).orElseThrow(RegistroNoEncontradoException::new);
    }

    @Override
    public List<ProductosTerminados> obtenerProductosTerminadosPorCliente(Long clienteId) {
        return productosTerminadosJpa.findByCliente_IdAndEstadoProductoTerminadoTrue(clienteId);
    }
}
