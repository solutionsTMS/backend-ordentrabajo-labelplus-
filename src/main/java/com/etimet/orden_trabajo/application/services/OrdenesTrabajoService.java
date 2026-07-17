package com.etimet.orden_trabajo.application.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etimet.orden_trabajo.application.mappers.OrdenesTrabajoMapper;
import com.etimet.orden_trabajo.domain.contratos.ClientesContrato;
import com.etimet.orden_trabajo.domain.contratos.OrdenesTrabajoContrato;
import com.etimet.orden_trabajo.domain.contratos.ProductosTerminadosContrato;
import com.etimet.orden_trabajo.domain.entities.Clientes;
import com.etimet.orden_trabajo.domain.entities.OrdenesTrabajo;
import com.etimet.orden_trabajo.domain.entities.ProductosTerminados;
import com.etimet.orden_trabajo.domain.enums.EstadoOrdenTrabajo;
import com.etimet.orden_trabajo.domain.enums.ModalidadProducto;
import com.etimet.orden_trabajo.domain.enums.TipoTrabajo;
import com.etimet.orden_trabajo.domain.records.PaginacionResponseRecord;
import com.etimet.orden_trabajo.domain.records.ordenestrabajo.OrdenesTrabajoRequestRecord;
import com.etimet.orden_trabajo.domain.records.ordenestrabajo.OrdenesTrabajoResponseRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdenesTrabajoService {

    private final OrdenesTrabajoContrato ordenesTrabajoContrato;
    private final ClientesContrato clientesContrato;
    private final ProductosTerminadosContrato productosTerminadosContrato;
    private final OrdenesTrabajoMapper ordenesTrabajoMapper;

    @Transactional
    public OrdenesTrabajoResponseRecord crearOrdenTrabajo(OrdenesTrabajoRequestRecord request) {
        log.info("Creando orden de trabajo: {}", request);
        validarReglasNegocio(request);

        Clientes cliente = clientesContrato.obtenerClientePorId(request.clienteId());
        OrdenesTrabajo orden = ordenesTrabajoMapper.deRequestADominio(request);
        orden.setCliente(cliente);
        orden.setCodigo(generarCodigo());
        orden.setEstado(EstadoOrdenTrabajo.Pendiente);
        aplicarProductoYDatosTecnicos(orden, request);
        aplicarArteAdjunto(orden, request);

        OrdenesTrabajo creada = ordenesTrabajoContrato.crearOrdenTrabajo(orden);
        log.info("Orden de trabajo creada: {}", creada.getCodigo());
        return ordenesTrabajoMapper.deDominioAResponse(creada);
    }

    @Transactional
    public OrdenesTrabajoResponseRecord actualizarOrdenTrabajo(Long id, OrdenesTrabajoRequestRecord request) {
        log.info("Actualizando orden de trabajo id={}: {}", id, request);
        OrdenesTrabajo orden = ordenesTrabajoContrato.obtenerOrdenTrabajoPorId(id);
        validarEditable(orden);
        validarReglasNegocio(request);

        ordenesTrabajoMapper.actualizarDominioDesdeRequest(request, orden);
        Clientes cliente = clientesContrato.obtenerClientePorId(request.clienteId());
        orden.setCliente(cliente);
        aplicarProductoYDatosTecnicos(orden, request);
        aplicarArteAdjunto(orden, request);

        OrdenesTrabajo actualizada = ordenesTrabajoContrato.crearOrdenTrabajo(orden);
        log.info("Orden de trabajo actualizada: {}", actualizada.getCodigo());
        return ordenesTrabajoMapper.deDominioAResponse(actualizada);
    }

    @Transactional
    public void anularOrdenTrabajo(Long id) {
        log.info("Anulando orden de trabajo: {}", id);
        OrdenesTrabajo orden = ordenesTrabajoContrato.obtenerOrdenTrabajoPorId(id);
        if (orden.getEstado() == EstadoOrdenTrabajo.Anulada) {
            throw new IllegalArgumentException("La orden de trabajo ya está anulada");
        }
        orden.setEstado(EstadoOrdenTrabajo.Anulada);
        ordenesTrabajoContrato.crearOrdenTrabajo(orden);
        log.info("Orden de trabajo anulada: {}", id);
    }

    @Transactional
    public OrdenesTrabajoResponseRecord obtenerOrdenTrabajoPorId(Long id) {
        log.info("Obteniendo orden de trabajo: {}", id);
        OrdenesTrabajo orden = ordenesTrabajoContrato.obtenerOrdenTrabajoPorId(id);
        return ordenesTrabajoMapper.deDominioAResponse(orden);
    }

    @Transactional
    public PaginacionResponseRecord<OrdenesTrabajoResponseRecord> obtenerOrdenesTrabajo(String buscar,
            Pageable pageable) {
        log.info("Obteniendo órdenes de trabajo con búsqueda: {}", buscar);
        Page<OrdenesTrabajoResponseRecord> pagina = ordenesTrabajoContrato.obtenerOrdenesTrabajo(buscar, pageable)
                .map(ordenesTrabajoMapper::deDominioAResponse);
        return new PaginacionResponseRecord<>(pagina.getContent(), pagina.getTotalElements(),
                pagina.getTotalPages(), pagina.getNumber());
    }

    private void validarReglasNegocio(OrdenesTrabajoRequestRecord request) {
        if (request.modalidad() == ModalidadProducto.Repetido
                && (request.productoId() == null)) {
            throw new IllegalArgumentException("El producto terminado es obligatorio cuando la modalidad es Repetido");
        }
        if (request.modalidad() == ModalidadProducto.Nuevo && request.productoId() != null) {
            throw new IllegalArgumentException("El producto terminado debe ser nulo cuando la modalidad es Nuevo");
        }
        if (request.tipoTrabajo() == TipoTrabajo.Termoencogible
                && (request.lyflat() == null || request.lyflat().compareTo(new BigDecimal("0.01")) < 0)) {
            throw new IllegalArgumentException("El lyflat es obligatorio y debe ser mayor a 0 para Termoencogible");
        }
    }

    private void validarEditable(OrdenesTrabajo orden) {
        if (orden.getEstado() == EstadoOrdenTrabajo.Anulada) {
            throw new IllegalArgumentException("No se puede editar una orden de trabajo anulada");
        }
        if (orden.getModalidad() == ModalidadProducto.Repetido
                && orden.getEstado() == EstadoOrdenTrabajo.Revision) {
            throw new IllegalArgumentException(
                    "No se puede editar una orden repetida que está en revisión");
        }
    }

    private void aplicarProductoYDatosTecnicos(OrdenesTrabajo orden, OrdenesTrabajoRequestRecord request) {
        if (request.modalidad() == ModalidadProducto.Repetido) {
            ProductosTerminados producto = productosTerminadosContrato
                    .obtenerProductoTerminadoPorId(request.productoId());
            if (!producto.getCliente().getId().equals(request.clienteId())) {
                throw new IllegalArgumentException("El producto terminado no pertenece al cliente seleccionado");
            }
            orden.setProducto(producto);
            orden.setDatosTecnicos(producto.getDatosTecnicos());
        } else {
            orden.setProducto(null);
            orden.setDatosTecnicos(null);
        }
    }

    private void aplicarArteAdjunto(OrdenesTrabajo orden, OrdenesTrabajoRequestRecord request) {
        if (Boolean.FALSE.equals(request.esImpreso())) {
            orden.setArteAdjuntoNombre(null);
        }
    }

    private String generarCodigo() {
        long siguiente = ordenesTrabajoContrato.contarOrdenesTrabajo() + 1;
        return String.format("OT-%04d", siguiente);
    }
}
