package com.etimet.orden_trabajo.domain.contratos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.etimet.orden_trabajo.domain.entities.ProductosTerminados;

public interface ProductosTerminadosContrato {

    ProductosTerminados crearProductoTerminado(ProductosTerminados producto);

    Page<ProductosTerminados> obtenerProductosTerminados(String buscar, Pageable pageable);

    ProductosTerminados obtenerProductoTerminadoPorId(Long id);

    List<ProductosTerminados> obtenerProductosTerminadosPorCliente(Long clienteId);
}
