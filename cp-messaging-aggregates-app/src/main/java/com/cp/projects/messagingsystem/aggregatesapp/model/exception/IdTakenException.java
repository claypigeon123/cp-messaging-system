package com.cp.projects.messagingsystem.aggregatesapp.model.exception;

public class IdTakenException extends AggregatesAppException {
    public IdTakenException() {
        super("ID already in use");
    }
}
