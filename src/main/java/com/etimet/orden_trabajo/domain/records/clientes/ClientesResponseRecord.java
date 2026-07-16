package com.etimet.orden_trabajo.domain.records.clientes;

import java.time.LocalDateTime;

public record ClientesResponseRecord(
        Long id,
        String codigoCliente,
        String identificacionCliente,
        String razonSocial,
        String nombreComercial,
        String direccion,
        String zona,
        String provincia,
        String ciudad,
        String parroquia,
        String telefono,
        String celular,
        String email,
        Integer dias,
        String sexo,
        String estadoCivil,
        String nombreUsuario,
        Boolean estadoCliente,
        Integer calificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {

}
