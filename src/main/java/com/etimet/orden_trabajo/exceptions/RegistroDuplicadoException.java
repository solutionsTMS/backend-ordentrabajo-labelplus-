package com.etimet.orden_trabajo.exceptions;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String registro) {
        super("El campo " + registro
                + " que intentas crear ya existe.");
    }
}
