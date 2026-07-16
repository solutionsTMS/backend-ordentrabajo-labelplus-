package com.etimet.orden_trabajo.application.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etimet.orden_trabajo.application.mappers.ClientesMapper;
import com.etimet.orden_trabajo.domain.contratos.ClientesContrato;
import com.etimet.orden_trabajo.domain.entities.Clientes;
import com.etimet.orden_trabajo.domain.records.PaginacionResponseRecord;
import com.etimet.orden_trabajo.domain.records.clientes.ClientesRequestRecord;
import com.etimet.orden_trabajo.domain.records.clientes.ClientesResponseRecord;
import com.etimet.orden_trabajo.domain.records.eventos.ClientesEventoDatos;
import com.etimet.orden_trabajo.domain.records.eventos.ClientesMensajeEvento;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientesService {
    private final ClientesContrato clientesContrato;
    private final ClientesMapper clientesMapper;

    @Transactional
    public void procesarClienteDesdeTaurus(ClientesMensajeEvento mensaje) {
        if (mensaje == null || mensaje.datos() == null) {
            throw new IllegalArgumentException("El mensaje de cliente recibido desde Taurus es inválido");
        }

        String accion = mensaje.accion() != null ? mensaje.accion().trim().toLowerCase() : "crear";
        ClientesEventoDatos datos = mensaje.datos();

        log.info("Procesando cliente desde Taurus. Acción: {}, Código: {}", accion, datos.codigoCliente());

        switch (accion) {
            case "eliminar" -> eliminarClientePorCodigo(datos.codigoCliente());
            case "crear", "actualizar" -> guardarClienteDesdeTaurus(datos);
            default -> {
                log.warn("Acción '{}' no reconocida. Se procesará como crear/actualizar.", accion);
                guardarClienteDesdeTaurus(datos);
            }
        }
    }

    private void guardarClienteDesdeTaurus(ClientesEventoDatos datos) {
        if (datos.codigoCliente() == null || datos.codigoCliente().isBlank()) {
            throw new IllegalArgumentException("El código del cliente es obligatorio en el mensaje de Taurus");
        }

        clientesContrato.obtenerClientePorCodigoCliente(datos.codigoCliente())
                .ifPresentOrElse(
                        cliente -> {
                            clientesMapper.actualizarDominioDesdeEvento(datos, cliente);
                            clientesContrato.crearCliente(cliente);
                            log.info("Cliente actualizado desde Taurus: {}", cliente.getCodigoCliente());
                        },
                        () -> {
                            Clientes cliente = clientesMapper.deEventoADominio(datos);
                            clientesContrato.crearCliente(cliente);
                            log.info("Cliente creado desde Taurus: {}", cliente.getCodigoCliente());
                        });
    }

    private void eliminarClientePorCodigo(String codigoCliente) {
        if (codigoCliente == null || codigoCliente.isBlank()) {
            throw new IllegalArgumentException("El código del cliente es obligatorio para eliminar");
        }

        clientesContrato.obtenerClientePorCodigoCliente(codigoCliente)
                .ifPresentOrElse(
                        cliente -> {
                            cliente.setEstadoCliente(false);
                            clientesContrato.crearCliente(cliente);
                            log.info("Cliente desactivado desde Taurus: {}", codigoCliente);
                        },
                        () -> log.warn("No se encontró cliente para eliminar con código: {}", codigoCliente));
    }

    @Transactional
    public ClientesResponseRecord crearCliente(ClientesRequestRecord request) {
        log.info("Creando cliente: {}", request);
        Clientes cliente = clientesMapper.deRequestADominio(request);
        Clientes clienteCreado = clientesContrato.crearCliente(cliente);
        ClientesResponseRecord response = clientesMapper.deDominioAResponse(clienteCreado);
        log.info("Cliente creado: {}", clienteCreado);
        return response;
    }

    @Transactional
    public ClientesResponseRecord actualizarCliente(Long id, ClientesRequestRecord request) {
        log.info("Actualizando cliente: {}", request);
        Clientes cliente = clientesContrato.obtenerClientePorId(id);
        clientesMapper.actualizarDominioDesdeRequest(request, cliente);
        Clientes clienteActualizado = clientesContrato.crearCliente(cliente);
        ClientesResponseRecord response = clientesMapper.deDominioAResponse(clienteActualizado);
        log.info("Cliente actualizado: {}", clienteActualizado);
        return response;
    }

    @Transactional
    public void eliminarCliente(Long id) {
        log.info("Eliminando cliente: {}", id);
        clientesContrato.obtenerClientePorId(id);
        clientesContrato.obtenerClientePorId(id).setEstadoCliente(false);
        clientesContrato.crearCliente(clientesContrato.obtenerClientePorId(id));
        log.info("Cliente eliminado: {}", id);
    }

    @Transactional
    public void activarCliente(Long id) {
        log.info("Activando cliente: {}", id);
        Clientes cliente = clientesContrato.obtenerClientePorId(id);
        cliente.setEstadoCliente(true);
        clientesContrato.crearCliente(cliente);
        log.info("Cliente activado: {}", id);
    }

    @Transactional
    public ClientesResponseRecord obtenerClientePorId(Long id) {
        log.info("Obteniendo cliente: {}", id);
        Clientes cliente = clientesContrato.obtenerClientePorId(id);
        ClientesResponseRecord response = clientesMapper.deDominioAResponse(cliente);
        log.info("Cliente obtenido: {}", cliente);
        return response;
    }

    @Transactional
    public PaginacionResponseRecord<ClientesResponseRecord> obtenerClientes(String buscar, Pageable pageable) {
        log.info("Obteniendo clientes con la busqueda: {}", buscar);
        Page<ClientesResponseRecord> clientes = clientesContrato.obtenerClientes(buscar, pageable)
                .map(clientesMapper::deDominioAResponse);
        log.info("Clientes obtenidos: {}", clientes);
        return new PaginacionResponseRecord<>(clientes.getContent(), clientes.getTotalElements(),
                clientes.getTotalPages(), clientes.getNumber());

    }

}
