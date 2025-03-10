package com.w2m.starshipregistry.core.ports.inbound;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;

public interface FindByIdStarshipPort {

    StarshipDtoNullable execute(Long id);

}
