package com.etimet.orden_trabajo.domain.records.clientes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientesRequestRecord(
        @NotBlank(message = "El código del cliente es obligatorio") @Size(max = 20) String codigoCliente,
        @NotBlank(message = "La identificación del cliente es obligatoria") @Size(max = 25) String identificacionCliente,
        @NotBlank(message = "La razón social es obligatoria") @Size(max = 300) String razonSocial,
        @Size(max = 300) String nombreComercial,
        @Size(max = 300) String direccion,
        @Size(max = 150) String zona,
        @Size(max = 150) String provincia,
        @Size(max = 150) String ciudad,
        @Size(max = 150) String parroquia,
        @Size(max = 15) String telefono,
        @Size(max = 15) String celular,
        @Size(max = 200) String email,
        Integer dias,
        @Size(max = 1) String sexo,
        @Size(max = 1) String estadoCivil,
        @Size(max = 50) String nombreUsuario,
        Integer calificacion) {

}
