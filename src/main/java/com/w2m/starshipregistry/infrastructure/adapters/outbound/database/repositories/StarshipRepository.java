package com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

@Repository
public interface StarshipRepository extends JpaRepository<StarshipEntity, Long> {
    List<StarshipEntity> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
