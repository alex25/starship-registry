package com.w2m.starshipregistry.core.ports.inbound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;

public interface FindAllStarshipPort {

    Page<StarshipDtoNullable> execute(Pageable pageable);


}
