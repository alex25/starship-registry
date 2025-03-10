package com.w2m.starshipregistry.core.ports.inbound;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;

public interface AddStarshipPort {

    StarshipDtoNullable execute(StarshipAddRequest starshipDto);
}