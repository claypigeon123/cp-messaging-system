package com.cp.projects.messagingsystem.cpmessagingdomain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String message;
}
