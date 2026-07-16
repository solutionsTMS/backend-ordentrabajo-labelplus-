package com.etimet.orden_trabajo.adapters.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.etimet.orden_trabajo.application.services.ClientesService;
import com.etimet.orden_trabajo.domain.records.PaginacionResponseRecord;
import com.etimet.orden_trabajo.domain.records.clientes.ClientesRequestRecord;
import com.etimet.orden_trabajo.domain.records.clientes.ClientesResponseRecord;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClientesController {
    private final ClientesService clientesService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClientesResponseRecord crearCliente(@RequestBody @Valid ClientesRequestRecord request) {
        return clientesService.crearCliente(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ClientesResponseRecord actualizarCliente(@PathVariable Long id,
            @RequestBody @Valid ClientesRequestRecord request) {
        return clientesService.actualizarCliente(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void eliminarCliente(@PathVariable Long id) {
        clientesService.eliminarCliente(id);
    }

    @PutMapping("/{id}/activar")
    @ResponseStatus(value = HttpStatus.OK)
    public void activarCliente(@PathVariable Long id) {
        clientesService.activarCliente(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ClientesResponseRecord obtenerClientePorId(@PathVariable Long id) {
        return clientesService.obtenerClientePorId(id);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public PaginacionResponseRecord<ClientesResponseRecord> obtenerClientes(
            @RequestParam(required = false) String buscar,
            Pageable pageable) {
        return clientesService.obtenerClientes(buscar, pageable);
    }

}
