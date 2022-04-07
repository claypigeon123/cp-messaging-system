package com.cp.projects.messagingsystem.aggregatesapp.model.exception;

public class IllegalAggregateTypeException extends AggregatesAppException {
    public IllegalAggregateTypeException() {
        super("Specified aggregate type does not exist");
    }
}
