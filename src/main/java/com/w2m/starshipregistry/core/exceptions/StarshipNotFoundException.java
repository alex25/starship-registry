package com.w2m.starshipregistry.core.exceptions;

public class StarshipNotFoundException extends RuntimeException {
    private final Long id;

    public StarshipNotFoundException(Long id) {
        super("Starship with ID %d not found".formatted(id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}