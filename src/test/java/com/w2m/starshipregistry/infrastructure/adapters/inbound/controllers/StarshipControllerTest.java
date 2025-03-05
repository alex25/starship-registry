package com.w2m.starshipregistry.infrastructure.adapters.inbound.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.starshipregistry.core.dtos.MovieDto;
import com.w2m.starshipregistry.core.dtos.StarshipDto;
import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.inbound.AddStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.DeleteStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.FindAllStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.FindByIdStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.FindByNameStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipPort;
import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.PageResponse;

@WebMvcTest(StarshipController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
public class StarshipControllerTest {

    @MockitoBean
    private FindAllStarshipPort findAllStarshipPort;

    @MockitoBean
    private FindByIdStarshipPort findByIdStarshipPort;

    @MockitoBean
    private FindByNameStarshipPort findByNameStarshipPort;

    @MockitoBean
    private DeleteStarshipPort deleteStarshipPort;

    @MockitoBean
    private ModifyStarshipPort modifyStarshipPort;

    @MockitoBean
    private AddStarshipPort addStarshipPort;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllStarships() throws JsonProcessingException, Exception {
        Pageable pageable = PageRequest.of(0, 10);
        MovieDto movie = new MovieDto(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship1 = new StarshipDto(1L, "Enterprise", movie);
        StarshipDtoNullable starship2 = new StarshipDto(2L, "Millennium Falcon", movie);

        List<StarshipDtoNullable> starships = List.of(starship1, starship2);
        Page<StarshipDtoNullable> starshipPage = new PageImpl<>(starships, pageable, starships.size());
        PageResponse<StarshipDtoNullable> response = PageResponse.from(starshipPage);

        given(findAllStarshipPort.execute(pageable)).willReturn(starshipPage);

        mockMvc.perform(get("/starships?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getStarshipById() throws JsonProcessingException, Exception {
        MovieDto movie = new MovieDto(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship = new StarshipDto(1L, "Enterprise", movie);

        given(findByIdStarshipPort.execute(1L)).willReturn(starship);

        mockMvc.perform(get("/starships/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(starship)));
    }

    @Test
    void getStarshipByIdThrowsStarshipNotFoundException() throws Exception {
        given(findByIdStarshipPort.execute(10L)).willThrow(new StarshipNotFoundException(10L));

        mockMvc.perform(get("/starships/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchStarshipsByName() throws JsonProcessingException, Exception {
        MovieDto movie = new MovieDto(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship = new StarshipDto(1L, "X-wing", movie);

        List<StarshipDtoNullable> starships = List.of(starship);

        given(findByNameStarshipPort.execute("wing")).willReturn(starships);

        mockMvc.perform(get("/starships/search?name=wing"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(starships)));
    }
}