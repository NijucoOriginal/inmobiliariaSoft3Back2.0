package com.jsebastian.eden.EdenSys.exceptions;

public class InmuebleException extends RuntimeException {
    public InmuebleException(String message) {
        super(message);
    }

    public InmuebleException(String message, Throwable cause) {
        super(message, cause);
    }

    public InmuebleException(Throwable cause) {
        super(cause);
    }
}