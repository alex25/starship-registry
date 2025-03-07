package com.w2m.starshipregistry.core.ports.inbound;

import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dtos.StarshipUpdateRequest;

public interface ModifyStarshipFromMqPort {

    StarshipDtoNullable execute(Long id, StarshipUpdateRequest updateRequest);
}