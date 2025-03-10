package com.w2m.starshipregistry.core.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NullStarshipDtoTest {

    @Test
    void idReturnsNull() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();

        // When
        Long id = dto.id();

        // Then
        assertNull(id, "ID should be null.");
    }

    @Test
    void nameReturnsNotFound() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();

        // When
        String name = dto.name();

        // Then
        assertEquals("Not Found", name, "Name should be 'Not Found'.");
    }

    @Test
    void movieDtoReturnsNull() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();

        // When
        MovieDtoNullable movieDto = dto.movieDto();

        // Then
        assertNull(movieDto, "Movie DTO should be null.");
    }

    @Test
    void isNullReturnsTrue() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();

        // When
        boolean isNull = dto.isNull();

        // Then
        assertTrue(isNull, "isNull should return true.");
    }

    @Test
    void withMovieReturnsNull() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();
        MovieDtoNullable newMovie = Mockito.mock(MovieDtoNullable.class);

        // When
        StarshipDtoNullable updatedDto = dto.withMovie(newMovie);

        // Then
        assertNull(updatedDto, "withMovie should return null.");
    }

    @Test
    void updateNameReturnsNull() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();
        String newName = "New Name";

        // When
        StarshipDtoNullable updatedDto = dto.updateName(newName);

        // Then
        assertNull(updatedDto, "updateName should return null.");
    }

    @Test
    void updateMovieIdReturnsNull() {
        // Given
        NullStarshipDto dto = new NullStarshipDto();
        Long newMovieId = 1L;

        // When
        StarshipDtoNullable updatedDto = dto.updateMovieId(newMovieId);

        // Then
        assertNull(updatedDto, "updateMovieId should return null.");
    }

}