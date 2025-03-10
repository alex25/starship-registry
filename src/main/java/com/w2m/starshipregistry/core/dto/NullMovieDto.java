package com.w2m.starshipregistry.core.dto;

public record NullMovieDto() implements MovieDtoNullable {

    @Override
    public Long id() {
        return null;
    }

    @Override
    public String title() {
        return "Not found";
    }

    @Override
    public Integer releaseYear() {
        return null;
    }

    @Override
    public Boolean isTvSeries() {
        return null;
    }
    
    @Override
    public boolean isNull() {
        return true; 
    }

}