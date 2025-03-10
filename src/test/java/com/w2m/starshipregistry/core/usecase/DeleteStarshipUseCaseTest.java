package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteStarshipUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @InjectMocks
    private DeleteStarshipUseCase deleteStarshipUseCase;

    private static final Long TEST_ID = 1L;

    @Test
    void executeWhenStarshipExistsShouldDeleteSuccessfully() {
        // Act
        deleteStarshipUseCase.execute(TEST_ID);

        // Assert
        verify(starshipDataPort).deleteStarship(eq(TEST_ID));
    }

    @Test
    void executeWhenEntityNotFoundShouldThrowStarshipNotFoundException() {
        // Arrange
        doThrow(new EntityNotFoundException("Not found"))
            .when(starshipDataPort).deleteStarship(TEST_ID);

        // Act & Assert
        assertThatThrownBy(() -> deleteStarshipUseCase.execute(TEST_ID))
            .isInstanceOf(StarshipNotFoundException.class)
            .hasMessageContaining("Starship with ID 1 not found");
    }

    @Test
    void executeWhenDependencyConflictShouldThrowStarshipDependencyException() {
        // Arrange
        doThrow(new DependencyConflictException("Conflict", new Exception()))
            .when(starshipDataPort).deleteStarship(TEST_ID);

        // Act & Assert
        assertThatThrownBy(() -> deleteStarshipUseCase.execute(TEST_ID))
            .isInstanceOf(StarshipDependencyException.class)
            .hasMessageContaining("Starship with ID 1 cannot be deleted due to existing dependencies")
            .hasCauseInstanceOf(DependencyConflictException.class);
    }
}