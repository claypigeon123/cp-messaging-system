package com.cp.projects.messagingsystem.cpmessagingdomain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CpMessagingSystemException extends RuntimeException {

    private final int status;

    public CpMessagingSystemException() {
        this.status = 500;
    }

    public CpMessagingSystemException(String message) {
        super(message);
        this.status = 500;
    }

    public CpMessagingSystemException(Throwable cause) {
        super(cause);
        this.status = 500;
    }

    public CpMessagingSystemException(String message, Throwable cause) {
        super(message, cause);
        this.status = 500;
    }

    public CpMessagingSystemException(int status) {
        this.status = status;
    }

    public CpMessagingSystemException(String message, int status) {
        super(message);
        this.status = status;
    }

    public CpMessagingSystemException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }
}
