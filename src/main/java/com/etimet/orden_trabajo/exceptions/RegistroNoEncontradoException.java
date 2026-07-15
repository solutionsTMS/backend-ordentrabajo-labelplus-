package com.etimet.orden_trabajo.exceptions;

public class RegistroNoEncontradoException extends RuntimeException {
    public RegistroNoEncontradoException() {
        super("No se encontró el registro solicitado.");
    }
}
