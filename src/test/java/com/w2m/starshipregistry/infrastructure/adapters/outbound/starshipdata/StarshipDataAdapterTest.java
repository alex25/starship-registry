package com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dtos.StarshipAddRequest;
import com.w2m.starshipregistry.core.dtos.StarshipDto;
import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.MovieRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.StarshipRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;
import com.w2m.starshipregistry.infrastructure.mappers.MovieMapper;
import com.w2m.starshipregistry.infrastructure.mappers.StarshipMapper;

@ExtendWith(MockitoExtension.class)
public class StarshipDataAdapterTest {

    @InjectMocks
    StarshipDataAdapter adapter;

    @Mock
    StarshipRepository starshipRepository;

    @Mock
    MovieRepository movieRepository;

    @Test
    void findStarship() {
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipEntity starship = StarshipEntity.builder().id(1L).name("Enterprise").movie(movie).build();
        List<StarshipEntity> response = List.of(starship);
        // Arrange
        given(starshipRepository.findByNameContainingIgnoreCase(any())).willReturn(response);

        // Act
        List<StarshipDtoNullable> result = adapter.searchStarshipsByName("Enterprise");

        // Assert
        assertEquals(List.of(new StarshipDto(1L, "Enterprise", MovieMapper.toDto(movie))), result);
    }

    @Test
    void searchStarshipsByNameWithNullName() {
        // Act
        List<StarshipDtoNullable> result = adapter.searchStarshipsByName(null);

        // Assert
        assertTrue(result.isEmpty());
        verify(starshipRepository, never()).findByNameContainingIgnoreCase(any());
    }

    @Test
    void findAllStarships() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipEntity starship1 = StarshipEntity.builder().id(1L).name("Enterprise").movie(movie).build();
        StarshipEntity starship2 = StarshipEntity.builder().id(2L).name("Millennium Falcon").movie(movie).build();

        List<StarshipEntity> starships = List.of(starship1, starship2);
        Page<StarshipEntity> starshipPage = new PageImpl<>(starships, pageable, starships.size());

        given(starshipRepository.findAll(pageable)).willReturn(starshipPage);

        // Act
        Page<StarshipDtoNullable> result = adapter.findAllStarships(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(new StarshipDto(1L, "Enterprise", MovieMapper.toDto(movie)), result.getContent().get(0));
        assertEquals(new StarshipDto(2L, "Millennium Falcon", MovieMapper.toDto(movie)), result.getContent().get(1));
    }

    @Test
    void findStarshipByIdWhenExists() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipEntity starship = StarshipEntity.builder().id(1L).name("Enterprise").movie(movie).build();

        given(starshipRepository.findById(1L)).willReturn(Optional.of(starship));
        
        // Act
        // Assert
        StarshipDtoNullable result = assertDoesNotThrow(() -> adapter.findStarshipById(1L));
        assertEquals(new StarshipDto(1L, "Enterprise", MovieMapper.toDto(movie)), result);
    }

    @Test
    void findStarshipByIdWhenNotExists() {
        // Arrange
        given(starshipRepository.findById(999L)).willReturn(Optional.empty());

        // Act
        Executable exec = () -> {
            adapter.findStarshipById(999L);
        };

        // Assert
        assertDoesNotThrow( exec);
    }

    @Test
    void addNewStarship() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        given(movieRepository.save(any())).willReturn(movie);
        StarshipAddRequest starshipAddRequest = new StarshipAddRequest("Enterprise III", 
        2L, "Starwars", 1986, false);
        StarshipDto starshipResultDto = new StarshipDto(11L, "Enterprise III", MovieMapper.toDto(movie));
        given(starshipRepository.save(any())).willReturn(StarshipMapper.toEntity(starshipResultDto));

        // Act
        StarshipDtoNullable result = adapter.addNewStarship(starshipAddRequest);

        // Assert
        assertNotNull(result.id());
    }

    @Test
    void addNewStarshipDuplicateKeyException() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipAddRequest starshipAddRequest = new StarshipAddRequest("Enterprise III", 
        2L, "Starwars", 1986, false);
        given(movieRepository.save(any())).willReturn(movie);
        given(starshipRepository.save(any()))
                .willThrow(new DataIntegrityViolationException("Database constraint violation"));

        // Act
        Executable exec = () -> {
            adapter.addNewStarship(starshipAddRequest);
        };

        // Assert
        assertThrows(DependencyConflictException.class, exec);
    }

    @Test
    void modifyStarship() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipDto starshipDto = new StarshipDto(1L, "Storm Shadow", MovieMapper.toDto(movie));
        given(starshipRepository.save(any())).willReturn(StarshipMapper.toEntity(starshipDto));

        // Act
        StarshipDtoNullable result = adapter.save(starshipDto);

        // Assert
        assertNotNull(result.id());
    }

    @Test
    void modifyStarshipDuplicateKeyException() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipDto starshipDto = new StarshipDto(0L, "X-Wing", MovieMapper.toDto(movie));
        given(starshipRepository.save(any()))
                .willThrow(new DataIntegrityViolationException("Database constraint violation"));

        // Act
        Executable exec = () -> {
            adapter.save(starshipDto);
        };

        // Assert
        assertThrows(DependencyConflictException.class, exec);
    }

    @Test
    void deleteStarship() {

        // Arrange
        given(starshipRepository.existsById(any())).willReturn(true);

        // Act
        Executable exec = () -> {
            adapter.deleteStarship(1L);
        };

        // Assert
        assertThatNoException().isThrownBy(exec::execute);
    }

    @Test
    void deleteStarshipEntityNotFoundException() {
        // Arrange
        given(starshipRepository.existsById(any())).willReturn(false);

        // Act

        // Assert
        assertThrows(StarshipNotFoundException.class,()-> adapter.deleteStarship(20L));
    }

    @Test
    void deleteStarshipDependencyConflictException() {
        // Arrange
        given(starshipRepository.existsById(any())).willReturn(true);
        doThrow(new DataIntegrityViolationException("Database constraint violation"))
                .when(starshipRepository).deleteById(any());
        // Act
        Executable exec = () -> {
            adapter.deleteStarship(20L);
        };

        // Assert
        assertThrows(DependencyConflictException.class, exec,
                "Starship with ID 20 cannot be deleted due to existing dependencies");
    }
}