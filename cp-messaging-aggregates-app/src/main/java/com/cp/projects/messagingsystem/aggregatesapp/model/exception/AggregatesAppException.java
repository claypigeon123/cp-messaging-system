package com.cp.projects.messagingsystem.aggregatesapp.model.exception;

public abstract class AggregatesAppException extends RuntimeException {
    public AggregatesAppException() {
    }

    public AggregatesAppException(String message) {
        super(message);
    }

    public AggregatesAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AggregatesAppException(Throwable cause) {
        super(cause);
    }
}
