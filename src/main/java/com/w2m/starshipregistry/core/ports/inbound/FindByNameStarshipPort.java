package com.w2m.starshipregistry.core.ports.inbound;

import java.util.List;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;

public interface FindByNameStarshipPort {

    List<StarshipDtoNullable> execute(String name);

}
