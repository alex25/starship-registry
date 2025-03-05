package com.w2m.starshipregistry.core.ports.inbound;

import com.w2m.starshipregistry.core.dtos.StarshipAddRequest;
import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;

public interface AddStarshipPort {

    StarshipDtoNullable execute(StarshipAddRequest starshipDto);
}