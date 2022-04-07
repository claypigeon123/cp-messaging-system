package com.cp.projects.messagingsystem.aggregatesapp.model.exception;

public class AggregateNotFoundException extends AggregatesAppException {
    public AggregateNotFoundException() {
        super("Aggregate not found");
    }
}
