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

import com.etimet.orden_trabajo.application.services.OrdenesTrabajoService;
import com.etimet.orden_trabajo.domain.records.PaginacionResponseRecord;
import com.etimet.orden_trabajo.domain.records.ordenestrabajo.OrdenesTrabajoRequestRecord;
import com.etimet.orden_trabajo.domain.records.ordenestrabajo.OrdenesTrabajoResponseRecord;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ordenes-trabajo")
@RequiredArgsConstructor
public class OrdenesTrabajoController {

    private final OrdenesTrabajoService ordenesTrabajoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenesTrabajoResponseRecord crearOrdenTrabajo(
            @RequestBody @Valid OrdenesTrabajoRequestRecord request) {
        return ordenesTrabajoService.crearOrdenTrabajo(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrdenesTrabajoResponseRecord actualizarOrdenTrabajo(@PathVariable Long id,
            @RequestBody @Valid OrdenesTrabajoRequestRecord request) {
        return ordenesTrabajoService.actualizarOrdenTrabajo(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void anularOrdenTrabajo(@PathVariable Long id) {
        ordenesTrabajoService.anularOrdenTrabajo(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrdenesTrabajoResponseRecord obtenerOrdenTrabajoPorId(@PathVariable Long id) {
        return ordenesTrabajoService.obtenerOrdenTrabajoPorId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginacionResponseRecord<OrdenesTrabajoResponseRecord> obtenerOrdenesTrabajo(
            @RequestParam(required = false) String buscar,
            Pageable pageable) {
        return ordenesTrabajoService.obtenerOrdenesTrabajo(buscar, pageable);
    }
}
