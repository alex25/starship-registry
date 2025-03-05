package com.w2m.starshipregistry.core.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface MovieDtoNullable {
    Long id();
    String title();
    Integer releaseYear();
    Boolean isTvSeries();

    @JsonIgnore
    default boolean isNull() {
        return false;
    }

}