package com.cp.projects.messagingsystem.aggregatesapp.model.exception;

import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;

public abstract class AggregatesAppException extends CpMessagingSystemException {
    public AggregatesAppException() {
    }

    public AggregatesAppException(String message) {
        super(message);
    }

    public AggregatesAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AggregatesAppException(int status) {
        super(status);
    }

    public AggregatesAppException(String message, int status) {
        super(message, status);
    }

    public AggregatesAppException(String message, Throwable cause, int status) {
        super(message, cause, status);
    }

    public AggregatesAppException(Throwable cause) {
        super(cause);
    }

}
