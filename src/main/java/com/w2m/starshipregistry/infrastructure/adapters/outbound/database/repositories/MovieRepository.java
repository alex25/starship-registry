package com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

}
