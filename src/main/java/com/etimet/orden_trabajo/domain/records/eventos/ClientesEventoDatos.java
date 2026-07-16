package com.etimet.orden_trabajo.domain.records.eventos;

public record ClientesEventoDatos(
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
        Integer calificacion) {

}
