package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.ports.outbound.AlertDataModificationPort;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

@ExtendWith(MockitoExtension.class)
class ModifyStarshipUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @Mock
    private AlertDataModificationPort alertDataModificationPort;

    @Mock
    private ModifyStarshipFromMqUseCase modifyStarshipFromMqUseCase;

    @InjectMocks
    private ModifyStarshipUseCase modifyStarshipUseCase;

    @Test
    void executeShouldModifyStarshipAndSendNotification() {
        // Arrange
        Long id = 1L;
        String newName = "Millennium Falcon";
        Long newMovieId = 5L;
        MovieDtoNullable newMovie = MovieDtoFactory.create(newMovieId, "star wars", 1987, false);
        StarshipUpdateRequest updateRequest = new StarshipUpdateRequest(newName, newMovieId);
        StarshipDtoNullable expectedDto = StarshipDtoFactory.create(id, newName, newMovie);

        when(modifyStarshipFromMqUseCase.execute(eq(id), eq(updateRequest))).thenReturn(expectedDto);

        // Act
        StarshipDtoNullable result = modifyStarshipUseCase.execute(id, updateRequest);

        // Assert
        verify(modifyStarshipFromMqUseCase).execute(id, updateRequest);
        verify(alertDataModificationPort).notifyStarshipModification(id, newName, newMovieId);
        assertThat(result).isEqualTo(expectedDto);
    }
}