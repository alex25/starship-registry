package com.w2m.starshipregistry.core.ports.inbound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;

public interface FindByNameStarshipPort {

    Page<StarshipDtoNullable> execute(String name, Pageable pageable);

}
