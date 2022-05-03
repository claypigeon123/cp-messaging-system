package com.cp.projects.messagingsystem.authserverapp.controller.advice;

import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private static final String ERRORS_PROPERTY = "violations";
    private static final String VIOLATION_MESSAGE = "Bad request - validation errors";

    private final ObjectMapper objectMapper;
    private final Clock clock;

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Mono<ExceptionResponse>> webClientResponseException(WebClientResponseException e) throws JsonProcessingException {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(Mono.just(objectMapper.readValue(e.getResponseBodyAsString(), ExceptionResponse.class)));
    }

    @ExceptionHandler(CpMessagingSystemException.class)
    public ResponseEntity<Mono<ExceptionResponse>> domainException(CpMessagingSystemException e) {
        return ResponseEntity
            .status(e.getStatus())
            .body(Mono.just(new ExceptionResponse(OffsetDateTime.now(clock), e.getStatus(), e.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> constraintViolationException(ConstraintViolationException e) {
        ExceptionResponse res = new ExceptionResponse(OffsetDateTime.now(clock), HttpStatus.BAD_REQUEST.value(), VIOLATION_MESSAGE);
        Map<String, Object> raw = objectMapper.convertValue(res, new TypeReference<>() {});

        raw.put(ERRORS_PROPERTY, new ArrayList<String>());
        e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .sorted()
            .forEach(message -> ((List<String>) raw.get(ERRORS_PROPERTY)).add(message));

        return Mono.just(raw);
    }
}
