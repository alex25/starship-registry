package com.w2m.starshipregistry.core.ports.inbound;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipUpdateRequest;

public interface ModifyStarshipFromMqPort {

    StarshipDtoNullable execute(Long id, StarshipUpdateRequest updateRequest);
}