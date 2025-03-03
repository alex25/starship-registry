package com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions;

public class DependencyConflictException extends RuntimeException {
    public DependencyConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}