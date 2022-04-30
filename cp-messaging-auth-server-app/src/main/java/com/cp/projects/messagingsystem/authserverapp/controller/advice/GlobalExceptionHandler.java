package com.cp.projects.messagingsystem.authserverapp.controller.advice;

import com.cp.projects.messagingsystem.cpmessagingdomain.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Mono<ExceptionResponse>> webClientResponseException(WebClientResponseException e) throws JsonProcessingException {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(Mono.just(objectMapper.readValue(e.getResponseBodyAsString(), ExceptionResponse.class)));
    }
}
