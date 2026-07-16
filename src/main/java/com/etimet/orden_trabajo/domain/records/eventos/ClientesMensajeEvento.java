package com.etimet.orden_trabajo.domain.records.eventos;

public record ClientesMensajeEvento(
        String accion,
        ClientesEventoDatos datos) {

}
