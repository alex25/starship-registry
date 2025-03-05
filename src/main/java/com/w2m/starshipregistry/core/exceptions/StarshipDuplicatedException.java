package com.w2m.starshipregistry.core.exceptions;

public class StarshipDuplicatedException extends StarshipConflictException {
    private final Long id;
    public StarshipDuplicatedException() {
        super("Starship with the same name already exists");
        this.id = 0L;
    }
    public StarshipDuplicatedException(Long id) {
        super("Starship with ID %d with the same name already exists".formatted(id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}