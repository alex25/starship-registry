package com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

@Repository
public interface StarshipRepository extends JpaRepository<StarshipEntity, Long> {
    Page<StarshipEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByName(String name);
}
