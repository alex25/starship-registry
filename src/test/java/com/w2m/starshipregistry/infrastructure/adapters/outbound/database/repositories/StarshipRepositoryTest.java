package com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

@SpringBootTest()
@Transactional
@Rollback(true)
public class StarshipRepositoryTest {

    @Autowired
    private StarshipRepository starshipRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findAllPagedSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<StarshipEntity> starshipPaged = starshipRepository.findAll(pageable);

        assertEquals(3, starshipPaged.getTotalPages());
    }

    @Test
    void findByidSuccess() {
        Optional<StarshipEntity> starship = starshipRepository.findById(2L);

        assertEquals("X-Wing", starship.get().getName());
    }

    @Test
    void findByNameContainingIgnoreCaseSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StarshipEntity> starships = starshipRepository.findByNameContainingIgnoreCase("wing", pageable);

        assertEquals("X-Wing", starships.getContent().getFirst().getName());
    }

    @Test
    void addNewStarshipSuccess() {

        MovieEntity movie = MovieEntity.builder().title("Star Trek").releaseYear(1979).isTvSeries(false).build();
        movie = movieRepository.save(movie);
        StarshipEntity starship1 = StarshipEntity.builder().name("USS Enterprise").movie(movie).build();

        StarshipEntity starshipPersisted = starshipRepository.save(starship1);

        assertEquals(starship1.getName(), starshipPersisted.getName());
    }

    @Test
    void modifyStarshipSuccess() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<StarshipEntity> starships = starshipRepository.findByNameContainingIgnoreCase("X-Wing", pageable);
        StarshipEntity starshipEntity = starships.getContent().getFirst();
        starshipEntity.setName("X-Wing II");

        StarshipEntity starshipEntityPersisted = starshipRepository.save(starshipEntity);

        assertEquals("X-Wing II", starshipEntityPersisted.getName());
    }

    @Test
    void deleteStarshipSuccess() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<StarshipEntity> starships = starshipRepository.findByNameContainingIgnoreCase("X-Wing", pageable);
        StarshipEntity starshipEntity = starships.getContent().getFirst();

        starshipRepository.delete(starshipEntity);

        starships = starshipRepository.findByNameContainingIgnoreCase("X-Wing", pageable);

        assertTrue(starships.isEmpty());
    }
}
