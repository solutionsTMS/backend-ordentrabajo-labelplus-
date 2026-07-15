package com.etimet.orden_trabajo.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.etimet.orden_trabajo.domain.records.ErrorResponseRecord;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponseRecord> handleBadCredentialsException(
            org.springframework.security.authentication.BadCredentialsException ex) {
        ErrorResponseRecord errorResponse = new ErrorResponseRecord("401", "Credenciales inválidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(RegistroNoEncontradoException.class)
    public ResponseEntity<ErrorResponseRecord> handleRegistroNoEncontradoException(RegistroNoEncontradoException ex) {
        ErrorResponseRecord errorResponse = new ErrorResponseRecord("404", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErrorResponseRecord> handleRegistroDuplicadoException(RegistroDuplicadoException ex) {
        ErrorResponseRecord errorResponse = new ErrorResponseRecord("409", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseRecord> handleValidationException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");
        ErrorResponseRecord errorResponse = new ErrorResponseRecord("400", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Evita que {@link ResponseStatusException} sea capturada por {@link Exception}
     * y devuelva 500
     * genérico (p. ej. validaciones de negocio como “sin niveles asignados”).
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseRecord> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String detail = ex.getReason();
        if (detail == null || detail.isBlank()) {
            detail = status.getReasonPhrase();
        }
        ErrorResponseRecord errorResponse = new ErrorResponseRecord(String.valueOf(status.value()), detail);
        return ResponseEntity.status(status).body(errorResponse);
    }

    // Manejador para violaciones de integridad (p. ej. constraint unique)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseRecord> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        String field = null;
        boolean esDuplicado = false;
        Throwable root = ex.getRootCause();
        if (root == null)
            root = ex.getCause();
        if (root != null && root.getMessage() != null) {
            String msg = root.getMessage();
            String msgLower = msg.toLowerCase();
            esDuplicado = msgLower.contains("duplicate key")
                    || msgLower.contains("already exists")
                    || msgLower.contains("duplicate entry")
                    || msgLower.contains("unique constraint")
                    || msgLower.contains("violates unique constraint");
            // Mensaje típico de Postgres: "duplicate key value violates unique constraint
            int idx = msg.indexOf("Key (");
            if (idx != -1) {
                int start = idx + "Key (".length();
                int end = msg.indexOf(')', start);
                if (end > start) {
                    field = msg.substring(start, end);
                }
            } else {
                // Mensaje típico de MySQL: "Duplicate entry 'x' for key 'nombre'" o "for key
                idx = msg.indexOf("for key '");
                if (idx != -1) {
                    int start = idx + "for key '".length();
                    int end = msg.indexOf('\'', start);
                    if (end > start) {
                        field = msg.substring(start, end);
                    }
                }
            }
        }

        String mensaje;
        if (esDuplicado && field != null && !field.isEmpty()) {
            mensaje = new RegistroDuplicadoException(field).getMessage();
        } else if (esDuplicado) {
            mensaje = "El registro que intentas crear ya existe. Por favor, verifica los datos e intenta nuevamente.";
        } else {
            mensaje = "No se pudo guardar el registro por una restriccion de datos. Contacta con el administrador del sistema.";
        }

        ErrorResponseRecord errorResponse = new ErrorResponseRecord("409", mensaje);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Agrega este método en tu RestExceptionHandler.java
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseRecord> handleIllegalArgumentException(IllegalArgumentException ex) {

        ErrorResponseRecord errorResponse = new ErrorResponseRecord("400", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseRecord> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponseRecord errorResponse = new ErrorResponseRecord("400", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseRecord> handleGeneralException(Exception ex) {
        // ESTO VA A OBLIGAR A LA CONSOLA DE VS CODE A MOSTRAR EL ERROR EN ROJO
        ex.printStackTrace();

        ErrorResponseRecord errorResponse = new ErrorResponseRecord("500", "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
