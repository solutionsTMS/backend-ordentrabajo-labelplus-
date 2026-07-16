package com.etimet.orden_trabajo.domain.contratos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.etimet.orden_trabajo.domain.entities.Clientes;

public interface ClientesContrato {
    Clientes crearCliente(Clientes cliente);

    Page<Clientes> obtenerClientes(String buscar, Pageable pageable);

    Clientes obtenerClientePorId(Long id);

    Optional<Clientes> obtenerClientePorCodigoCliente(String codigoCliente);

}
