package com.etimet.orden_trabajo.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.etimet.orden_trabajo.domain.entities.ProductosTerminados;
import com.etimet.orden_trabajo.domain.records.productosterminados.ProductosTerminadosRequestRecord;
import com.etimet.orden_trabajo.domain.records.productosterminados.ProductosTerminadosResponseRecord;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductosTerminadosMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    ProductosTerminadosResponseRecord deDominioAResponse(ProductosTerminados producto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "estadoProductoTerminado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    ProductosTerminados deRequestADominio(ProductosTerminadosRequestRecord request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "estadoProductoTerminado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarDominioDesdeRequest(ProductosTerminadosRequestRecord request,
            @MappingTarget ProductosTerminados entity);
}
