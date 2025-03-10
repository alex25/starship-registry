package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

@ExtendWith(MockitoExtension.class)
class FindAllStarshipUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @InjectMocks
    private FindAllStarshipUseCase findAllStarshipUseCase;

    private static final Pageable TEST_PAGEABLE = PageRequest.of(0, 10);
    private List<StarshipDtoNullable> sampleStarships;

    @BeforeEach
    void setUp() {
        sampleStarships = List.of(
            createStarshipDto(1L, "Millennium Falcon", 4L),
            createStarshipDto(2L, "X-Wing", 5L)
        );
    }

    @Test
    void executeShouldReturnPaginatedStarships() {
        // Arrange
        Page<StarshipDtoNullable> expectedPage = new PageImpl<>(sampleStarships, TEST_PAGEABLE, 2);
        when(starshipDataPort.findAllStarships(TEST_PAGEABLE)).thenReturn(expectedPage);

        // Act
        Page<StarshipDtoNullable> result = findAllStarshipUseCase.execute(TEST_PAGEABLE);

        // Assert
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(2);
        verify(starshipDataPort).findAllStarships(eq(TEST_PAGEABLE));
    }

    @Test
    void executeWhenNoStarshipsExistShouldReturnEmptyPage() {
        // Arrange
        Page<StarshipDtoNullable> emptyPage = new PageImpl<>(Collections.emptyList(), TEST_PAGEABLE, 0);
        when(starshipDataPort.findAllStarships(TEST_PAGEABLE)).thenReturn(emptyPage);

        // Act
        Page<StarshipDtoNullable> result = findAllStarshipUseCase.execute(TEST_PAGEABLE);

        // Assert
        assertThat(result).isEmpty();
        verify(starshipDataPort).findAllStarships(eq(TEST_PAGEABLE));
    }

    // Helper method to create test DTOs
    private StarshipDtoNullable createStarshipDto(Long id, String name, Long movieId) {
        return StarshipDtoFactory.create(id, name, MovieDtoFactory.create(movieId, "Test Movie", 2023, false));
    }
}