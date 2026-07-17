package com.etimet.orden_trabajo.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.etimet.orden_trabajo.domain.entities.OrdenesTrabajo;
import com.etimet.orden_trabajo.domain.records.ordenestrabajo.OrdenesTrabajoRequestRecord;
import com.etimet.orden_trabajo.domain.records.ordenestrabajo.OrdenesTrabajoResponseRecord;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdenesTrabajoMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "clienteNombre", source = "cliente.razonSocial")
    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoCodigo", source = "producto.codigo")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    OrdenesTrabajoResponseRecord deDominioAResponse(OrdenesTrabajo orden);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "datosTecnicos", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    OrdenesTrabajo deRequestADominio(OrdenesTrabajoRequestRecord request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "datosTecnicos", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarDominioDesdeRequest(OrdenesTrabajoRequestRecord request, @MappingTarget OrdenesTrabajo entity);
}
