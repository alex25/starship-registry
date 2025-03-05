package com.w2m.starshipregistry.core.ports.inbound;

import java.util.List;

import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;

public interface FindByNameStarshipPort {

    List<StarshipDtoNullable> execute(String name);

}
