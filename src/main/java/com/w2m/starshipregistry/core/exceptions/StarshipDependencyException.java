package com.w2m.starshipregistry.core.exceptions;

public class StarshipDependencyException extends StarshipConflictException {
    public StarshipDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}