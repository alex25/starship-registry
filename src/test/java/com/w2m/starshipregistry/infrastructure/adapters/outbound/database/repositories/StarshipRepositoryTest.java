package com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
    void findAllPaged() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<StarshipEntity> starshipPaged = starshipRepository.findAll(pageable);

        assertEquals(3, starshipPaged.getTotalPages());
    }

    @Test
    void findByid() {
        Optional<StarshipEntity> starship = starshipRepository.findById(2L);

        assertEquals("X-Wing", starship.get().getName());
    }

    @Test
    void findByNameContainingIgnoreCase() {
        List<StarshipEntity> starshipList = starshipRepository.findByNameContainingIgnoreCase("wing");

        assertEquals("X-Wing", starshipList.getFirst().getName());
    }

    @Test
    void addNewStarship() {

        MovieEntity movie = MovieEntity.builder().title("Star Trek").releaseYear(1979).isTvSeries(false).build();
        movie = movieRepository.save(movie);
        StarshipEntity starship1 = StarshipEntity.builder().name("USS Enterprise").movie(movie).build();

        StarshipEntity starshipPersisted = starshipRepository.save(starship1);

        assertEquals(starship1.getName(), starshipPersisted.getName());
    }

    @Test
    void modifyStarship() {

        List<StarshipEntity> starshipList = starshipRepository.findByNameContainingIgnoreCase("X-Wing");
        StarshipEntity starshipEntity = starshipList.getFirst();
        starshipEntity.setName("X-Wing II");

        StarshipEntity starshipEntityPersisted = starshipRepository.save(starshipEntity);

        assertEquals("X-Wing II", starshipEntityPersisted.getName());
    }

    @Test
    void deleteStarship() {

        List<StarshipEntity> starshipList = starshipRepository.findByNameContainingIgnoreCase("X-Wing");
        StarshipEntity starshipEntity = starshipList.getFirst();

        starshipRepository.delete(starshipEntity);

        starshipList = starshipRepository.findByNameContainingIgnoreCase("X-Wing");

        assertTrue(starshipList.isEmpty());
    }
}
