package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipDuplicatedException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;

@ExtendWith(MockitoExtension.class)
class AddStarshipUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @InjectMocks
    private AddStarshipUseCase addStarshipUseCase;

    private static final String TEST_NAME = "Millennium Falcon";
    private static final Long TEST_MOVIE_ID = 4L;
    private StarshipAddRequest testRequest;
    private StarshipDtoNullable testDto;

    @BeforeEach
    void setUp() {
        testRequest = new StarshipAddRequest(TEST_NAME, TEST_MOVIE_ID, 
            "New Starwars", 2025, false);
        testDto = StarshipDtoFactory.create(1L, TEST_NAME, 
            MovieDtoFactory.create(TEST_MOVIE_ID, "Star Wars", 1977, false));
    }

    @Test
    void executeWhenValidRequestShouldAddStarship() {
        // Arrange
        when(starshipDataPort.existsByName(TEST_NAME)).thenReturn(false);
        when(starshipDataPort.addNewStarship(testRequest)).thenReturn(testDto);

        // Act
        StarshipDtoNullable result = addStarshipUseCase.execute(testRequest);

        // Assert
        assertThat(result).isEqualTo(testDto);
        verify(starshipDataPort).existsByName(TEST_NAME);
        verify(starshipDataPort).addNewStarship(testRequest);
    }

    @Test
    void executeWhenDuplicateNameShouldThrowException() {
        // Arrange
        when(starshipDataPort.existsByName(TEST_NAME)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> addStarshipUseCase.execute(testRequest))
            .isInstanceOf(StarshipDuplicatedException.class)
            .hasMessageContaining("already exists");
        
        verify(starshipDataPort).existsByName(TEST_NAME);
        verify(starshipDataPort, never()).addNewStarship(any());
    }

    @Test
    void executeWhenDependencyConflictShouldThrowWrappedException() {
        // Arrange
        when(starshipDataPort.existsByName(TEST_NAME)).thenReturn(false);
        when(starshipDataPort.addNewStarship(testRequest))
            .thenThrow(new DependencyConflictException("Database constraint failed", new Exception()));

        // Act & Assert
        assertThatThrownBy(() -> addStarshipUseCase.execute(testRequest))
            .isInstanceOf(StarshipDependencyException.class)
            .hasMessageContaining("Starship cannot be add due to existing dependencies")
            .hasCauseInstanceOf(DependencyConflictException.class);
        
        verify(starshipDataPort).existsByName(TEST_NAME);
        verify(starshipDataPort).addNewStarship(testRequest);
    }
}
