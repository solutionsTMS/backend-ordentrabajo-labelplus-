package com.etimet.orden_trabajo.application.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etimet.orden_trabajo.application.mappers.ProductosTerminadosMapper;
import com.etimet.orden_trabajo.domain.contratos.ClientesContrato;
import com.etimet.orden_trabajo.domain.contratos.ProductosTerminadosContrato;
import com.etimet.orden_trabajo.domain.entities.Clientes;
import com.etimet.orden_trabajo.domain.entities.ProductosTerminados;
import com.etimet.orden_trabajo.domain.records.PaginacionResponseRecord;
import com.etimet.orden_trabajo.domain.records.productosterminados.ProductosTerminadosRequestRecord;
import com.etimet.orden_trabajo.domain.records.productosterminados.ProductosTerminadosResponseRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductosTerminadosService {

    private final ProductosTerminadosContrato productosTerminadosContrato;
    private final ClientesContrato clientesContrato;
    private final ProductosTerminadosMapper productosTerminadosMapper;

    @Transactional
    public ProductosTerminadosResponseRecord crearProductoTerminado(ProductosTerminadosRequestRecord request) {
        log.info("Creando producto terminado: {}", request);
        Clientes cliente = clientesContrato.obtenerClientePorId(request.clienteId());
        ProductosTerminados producto = productosTerminadosMapper.deRequestADominio(request);
        producto.setCliente(cliente);
        ProductosTerminados creado = productosTerminadosContrato.crearProductoTerminado(producto);
        ProductosTerminadosResponseRecord response = productosTerminadosMapper.deDominioAResponse(creado);
        log.info("Producto terminado creado: {}", creado.getCodigo());
        return response;
    }

    @Transactional
    public ProductosTerminadosResponseRecord actualizarProductoTerminado(Long id,
            ProductosTerminadosRequestRecord request) {
        log.info("Actualizando producto terminado id={}: {}", id, request);
        ProductosTerminados producto = productosTerminadosContrato.obtenerProductoTerminadoPorId(id);
        productosTerminadosMapper.actualizarDominioDesdeRequest(request, producto);
        Clientes cliente = clientesContrato.obtenerClientePorId(request.clienteId());
        producto.setCliente(cliente);
        ProductosTerminados actualizado = productosTerminadosContrato.crearProductoTerminado(producto);
        return productosTerminadosMapper.deDominioAResponse(actualizado);
    }

    @Transactional
    public void eliminarProductoTerminado(Long id) {
        log.info("Eliminando producto terminado: {}", id);
        ProductosTerminados producto = productosTerminadosContrato.obtenerProductoTerminadoPorId(id);
        producto.setEstadoProductoTerminado(false);
        productosTerminadosContrato.crearProductoTerminado(producto);
        log.info("Producto terminado eliminado: {}", id);
    }

    @Transactional
    public void activarProductoTerminado(Long id) {
        log.info("Activando producto terminado: {}", id);
        ProductosTerminados producto = productosTerminadosContrato.obtenerProductoTerminadoPorId(id);
        producto.setEstadoProductoTerminado(true);
        productosTerminadosContrato.crearProductoTerminado(producto);
        log.info("Producto terminado activado: {}", id);
    }

    @Transactional
    public ProductosTerminadosResponseRecord obtenerProductoTerminadoPorId(Long id) {
        log.info("Obteniendo producto terminado: {}", id);
        ProductosTerminados producto = productosTerminadosContrato.obtenerProductoTerminadoPorId(id);
        return productosTerminadosMapper.deDominioAResponse(producto);
    }

    @Transactional
    public PaginacionResponseRecord<ProductosTerminadosResponseRecord> obtenerProductosTerminados(String buscar,
            Pageable pageable) {
        log.info("Obteniendo productos terminados con búsqueda: {}", buscar);
        Page<ProductosTerminadosResponseRecord> pagina = productosTerminadosContrato
                .obtenerProductosTerminados(buscar, pageable)
                .map(productosTerminadosMapper::deDominioAResponse);
        return new PaginacionResponseRecord<>(pagina.getContent(), pagina.getTotalElements(),
                pagina.getTotalPages(), pagina.getNumber());
    }

    @Transactional
    public List<ProductosTerminadosResponseRecord> obtenerProductosTerminadosPorCliente(Long clienteId) {
        log.info("Obteniendo productos terminados del cliente: {}", clienteId);
        clientesContrato.obtenerClientePorId(clienteId);
        return productosTerminadosContrato.obtenerProductosTerminadosPorCliente(clienteId).stream()
                .map(productosTerminadosMapper::deDominioAResponse)
                .toList();
    }
}
