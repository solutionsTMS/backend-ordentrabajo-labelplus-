package com.etimet.orden_trabajo.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.etimet.orden_trabajo.domain.entities.Clientes;
import com.etimet.orden_trabajo.domain.records.clientes.ClientesRequestRecord;
import com.etimet.orden_trabajo.domain.records.clientes.ClientesResponseRecord;
import com.etimet.orden_trabajo.domain.records.eventos.ClientesEventoDatos;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientesMapper {
    ClientesResponseRecord deDominioAResponse(Clientes cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "estadoCliente", ignore = true)
    Clientes deRequestADominio(ClientesRequestRecord request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "estadoCliente", ignore = true)
    void actualizarDominioDesdeRequest(ClientesRequestRecord request, @MappingTarget Clientes entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Clientes deEventoADominio(ClientesEventoDatos datos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarDominioDesdeEvento(ClientesEventoDatos datos, @MappingTarget Clientes entity);

}
