package com.etimet.orden_trabajo.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.etimet.orden_trabajo.domain.contratos.ClientesContrato;
import com.etimet.orden_trabajo.domain.entities.Clientes;
import com.etimet.orden_trabajo.exceptions.RegistroNoEncontradoException;
import com.etimet.orden_trabajo.infrastructure.repository.jpa.ClientesJpa;
import com.etimet.orden_trabajo.infrastructure.specifications.ClientesSpecification;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClientesRepository implements ClientesContrato {

    private final ClientesJpa clientesJpa;

    @Override
    public Clientes crearCliente(Clientes cliente) {
        return clientesJpa.save(cliente);
    }

    @Override
    public Page<Clientes> obtenerClientes(String buscar, Pageable pageable) {
        List<String> columnasABuscar = List.of(
                "codigoCliente", "identificacionCliente", "razonSocial", "nombreComercial",
                "direccion", "zona", "provincia", "ciudad", "parroquia", "telefono", "celular",
                "email", "sexo", "estadoCivil", "nombreUsuario");
        return clientesJpa.findAll(Specification
                .where(ClientesSpecification.busquedaGlobal(buscar, columnasABuscar))
                .and(ClientesSpecification.activos()), pageable);
    }

    @Override
    public Clientes obtenerClientePorId(Long id) {
        return clientesJpa.findById(id).orElseThrow(RegistroNoEncontradoException::new);
    }

    @Override
    public Optional<Clientes> obtenerClientePorCodigoCliente(String codigoCliente) {
        return clientesJpa.findByCodigoCliente(codigoCliente);
    }

}
