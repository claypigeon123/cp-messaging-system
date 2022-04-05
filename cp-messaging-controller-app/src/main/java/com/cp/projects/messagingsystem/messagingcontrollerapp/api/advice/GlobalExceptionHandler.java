package com.cp.projects.messagingsystem.messagingcontrollerapp.api.advice;

import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(CpMessagingSystemException.class)
    public Mono<ResponseEntity<String>> handleDomainException(CpMessagingSystemException e) {
        return Mono.just(ResponseEntity.status(e.getStatus()).body(e.getMessage()));
    }
}
