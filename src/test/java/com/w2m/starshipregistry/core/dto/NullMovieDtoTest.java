package com.w2m.starshipregistry.core.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class NullMovieDtoTest {

    @Test
    void idShouldReturnNull() {
        // Arrange
        NullMovieDto nullMovieDto = new NullMovieDto();

        // Act
        Long id = nullMovieDto.id();

        // Assert
        assertThat(id).isNull();
    }

    @Test
    void titleShouldReturnNotFound() {
        // Arrange
        NullMovieDto nullMovieDto = new NullMovieDto();

        // Act
        String title = nullMovieDto.title();

        // Assert
        assertThat(title).isEqualTo("Not found");
    }

    @Test
    void releaseYearShouldReturnNull() {
        // Arrange
        NullMovieDto nullMovieDto = new NullMovieDto();

        // Act
        Integer releaseYear = nullMovieDto.releaseYear();

        // Assert
        assertThat(releaseYear).isNull();
    }

    @Test
    void isTvSeriesShouldReturnNull() {
        // Arrange
        NullMovieDto nullMovieDto = new NullMovieDto();

        // Act
        Boolean isTvSeries = nullMovieDto.isTvSeries();

        // Assert
        assertThat(isTvSeries).isNull();
    }

    @Test
    void isNullShouldReturnTrue() {
        // Arrange
        NullMovieDto nullMovieDto = new NullMovieDto();

        // Act
        boolean isNull = nullMovieDto.isNull();

        // Assert
        assertThat(isNull).isTrue();
    }
}