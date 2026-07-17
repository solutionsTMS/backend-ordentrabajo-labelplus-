package com.etimet.orden_trabajo.adapters.controllers;

import java.util.List;

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

import com.etimet.orden_trabajo.application.services.ProductosTerminadosService;
import com.etimet.orden_trabajo.domain.records.PaginacionResponseRecord;
import com.etimet.orden_trabajo.domain.records.productosterminados.ProductosTerminadosRequestRecord;
import com.etimet.orden_trabajo.domain.records.productosterminados.ProductosTerminadosResponseRecord;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos-terminados")
@RequiredArgsConstructor
public class ProductosTerminadosController {

    private final ProductosTerminadosService productosTerminadosService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductosTerminadosResponseRecord crearProductoTerminado(
            @RequestBody @Valid ProductosTerminadosRequestRecord request) {
        return productosTerminadosService.crearProductoTerminado(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductosTerminadosResponseRecord actualizarProductoTerminado(@PathVariable Long id,
            @RequestBody @Valid ProductosTerminadosRequestRecord request) {
        return productosTerminadosService.actualizarProductoTerminado(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProductoTerminado(@PathVariable Long id) {
        productosTerminadosService.eliminarProductoTerminado(id);
    }

    @PutMapping("/{id}/activar")
    @ResponseStatus(HttpStatus.OK)
    public void activarProductoTerminado(@PathVariable Long id) {
        productosTerminadosService.activarProductoTerminado(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductosTerminadosResponseRecord obtenerProductoTerminadoPorId(@PathVariable Long id) {
        return productosTerminadosService.obtenerProductoTerminadoPorId(id);
    }

    @GetMapping(params = "clienteId")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductosTerminadosResponseRecord> obtenerProductosTerminadosPorCliente(
            @RequestParam Long clienteId) {
        return productosTerminadosService.obtenerProductosTerminadosPorCliente(clienteId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginacionResponseRecord<ProductosTerminadosResponseRecord> obtenerProductosTerminados(
            @RequestParam(required = false) String buscar,
            Pageable pageable) {
        return productosTerminadosService.obtenerProductosTerminados(buscar, pageable);
    }
}
