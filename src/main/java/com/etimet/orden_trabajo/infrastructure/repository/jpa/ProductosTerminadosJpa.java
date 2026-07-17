package com.etimet.orden_trabajo.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.etimet.orden_trabajo.domain.entities.ProductosTerminados;

public interface ProductosTerminadosJpa
        extends JpaRepository<ProductosTerminados, Long>, JpaSpecificationExecutor<ProductosTerminados> {

    Optional<ProductosTerminados> findByCodigo(String codigo);

    List<ProductosTerminados> findByCliente_IdAndEstadoProductoTerminadoTrue(Long clienteId);
}
