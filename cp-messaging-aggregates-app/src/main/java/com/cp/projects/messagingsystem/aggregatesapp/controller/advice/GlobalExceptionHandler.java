package com.cp.projects.messagingsystem.aggregatesapp.controller.advice;

import com.cp.projects.messagingsystem.aggregatesapp.model.exception.AggregateNotFoundException;
import com.cp.projects.messagingsystem.aggregatesapp.model.exception.IdTakenException;
import com.cp.projects.messagingsystem.aggregatesapp.model.exception.IllegalAggregateTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAggregateTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> illegalAggregateType(IllegalAggregateTypeException e) {
        return Mono.just(e.getMessage());
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> aggregateNotFound(AggregateNotFoundException e) {
        return Mono.just(e.getMessage());
    }

    @ExceptionHandler(IdTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<String> idTaken(IdTakenException e) {
        return Mono.just(e.getMessage());
    }
}
