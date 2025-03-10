package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

@ExtendWith(MockitoExtension.class)
public class FindByIdStarshipUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @InjectMocks
    private FindByIdStarshipUseCase findByIdStarshipUseCase;

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Millennium Falcon";
    private static final Long TEST_MOVIE_ID = 4L;

    private StarshipDtoNullable validStarshipDto;

    @BeforeEach
    void setUp() {
        MovieDtoNullable existingMovie = MovieDtoFactory.create(TEST_MOVIE_ID, "star wars", 1987, false);
        validStarshipDto = StarshipDtoFactory.create(TEST_ID, TEST_NAME, existingMovie);
    }

    @Test
    void executeWhenStarshipExistsShouldReturnDto() {
        // Arrange
        when(starshipDataPort.findStarshipById(TEST_ID)).thenReturn(validStarshipDto);

        // Act
        StarshipDtoNullable result = findByIdStarshipUseCase.execute(TEST_ID);

        // Assert
        assertThat(result).isEqualTo(validStarshipDto);
        verify(starshipDataPort).findStarshipById(eq(TEST_ID));
    }

    @Test
    void executeWhenStarshipNotFoundShouldThrowException() {
        // Arrange
        StarshipDtoNullable nullDto = StarshipDtoFactory.nullInstance();;
        when(starshipDataPort.findStarshipById(TEST_ID)).thenReturn(nullDto);

        // Act & Assert
        assertThatThrownBy(() -> findByIdStarshipUseCase.execute(TEST_ID))
                .isInstanceOf(StarshipNotFoundException.class)
                .hasMessageContaining("Starship with ID 1 not found");

        verify(starshipDataPort).findStarshipById(eq(TEST_ID));
    }
}