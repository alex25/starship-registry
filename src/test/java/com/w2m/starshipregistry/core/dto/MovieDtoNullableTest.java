package com.w2m.starshipregistry.core.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class MovieDtoNullableTest {

    private static class TestMovieDto implements MovieDtoNullable {
        private final Long id;
        private final String title;
        private final Integer releaseYear;
        private final Boolean isTvSeries;

        public TestMovieDto(Long id, String title, Integer releaseYear, Boolean isTvSeries) {
            this.id = id;
            this.title = title;
            this.releaseYear = releaseYear;
            this.isTvSeries = isTvSeries;
        }

        @Override
        public Long id() { return id; }

        @Override
        public String title() { return title; }

        @Override
        public Integer releaseYear() { return releaseYear; }

        @Override
        public Boolean isTvSeries() { return isTvSeries; }
    }

    @Test
    void isNullReturnsFalse() {
        MovieDtoNullable dto = new TestMovieDto(1L, "Test", 2023, false);
        assertFalse(dto.isNull(), "isNull() should return false by default");
    }

    @Test
    void isNullWithAllNullFields() {
        MovieDtoNullable dto = new TestMovieDto(null, null, null, null);
        assertFalse(dto.isNull(), "isNull() should return false even if all fields are null");
    }

}