package com.etimet.orden_trabajo.infrastructure.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.etimet.orden_trabajo.domain.entities.Clientes;

public interface ClientesJpa extends JpaRepository<Clientes, Long>, JpaSpecificationExecutor<Clientes> {
    Optional<Clientes> findByCodigoCliente(String codigoCliente);

}
