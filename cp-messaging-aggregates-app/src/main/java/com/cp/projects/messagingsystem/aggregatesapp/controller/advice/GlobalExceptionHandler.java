package com.cp.projects.messagingsystem.aggregatesapp.controller.advice;

import com.cp.projects.messagingsystem.aggregatesapp.model.exception.AggregateNotFoundException;
import com.cp.projects.messagingsystem.aggregatesapp.model.exception.IdTakenException;
import com.cp.projects.messagingsystem.aggregatesapp.model.exception.IllegalAggregateTypeException;
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.OffsetDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(IllegalAggregateTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ExceptionResponse> illegalAggregateType(IllegalAggregateTypeException e) {
        return Mono.just(
            new ExceptionResponse(OffsetDateTime.now(clock), HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ExceptionResponse> aggregateNotFound(AggregateNotFoundException e) {
        return Mono.just(
            new ExceptionResponse(OffsetDateTime.now(clock), HttpStatus.NOT_FOUND.value(), e.getMessage())
        );
    }

    @ExceptionHandler(IdTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ExceptionResponse> idTaken(IdTakenException e) {
        return Mono.just(
            new ExceptionResponse(OffsetDateTime.now(clock), HttpStatus.CONFLICT.value(), e.getMessage())
        );
    }
}
