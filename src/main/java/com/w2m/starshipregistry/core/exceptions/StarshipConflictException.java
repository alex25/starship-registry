package com.w2m.starshipregistry.core.exceptions;

public abstract class StarshipConflictException extends RuntimeException {
    public StarshipConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    public StarshipConflictException(String message) {
        super(message);
    }
}