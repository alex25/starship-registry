package com.w2m.starshipregistry.infrastructure.adapters.inbound.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.exceptions.StarshipDuplicatedException;
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

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Test Ship";

    @Test
    void getAllStarships() throws JsonProcessingException, Exception {
        Pageable pageable = PageRequest.of(0, 10);
        MovieDtoNullable movie = MovieDtoFactory.create(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship1 = StarshipDtoFactory.create(1L, "Enterprise", movie);
        StarshipDtoNullable starship2 = StarshipDtoFactory.create(2L, "Millennium Falcon", movie);

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
        MovieDtoNullable movie = MovieDtoFactory.create(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship = StarshipDtoFactory.create(1L, "Enterprise", movie);

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
        MovieDtoNullable movie = MovieDtoFactory.create(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship = StarshipDtoFactory.create(1L, "X-wing", movie);

        List<StarshipDtoNullable> starships = List.of(starship);

        given(findByNameStarshipPort.execute("wing")).willReturn(starships);

        mockMvc.perform(get("/starships/search?name=wing"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(starships)));
    }

    // DELETE /starships/{id}
    @Test
    void deleteStarshipSuccess() throws Exception {
        doNothing().when(deleteStarshipPort).execute(TEST_ID);
        
        mockMvc.perform(delete("/starships/%d".formatted(TEST_ID) ))
               .andExpect(status().isNoContent());
    }

    @Test
    void deleteStarshipNotFound() throws Exception {
        doThrow(new StarshipNotFoundException(TEST_ID)).when(deleteStarshipPort).execute(TEST_ID);
        
        mockMvc.perform(delete("/starships/{id}", TEST_ID))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.title").value("Starship Not Found"));
    }

    // PUT /starships/{id}
    @Test
    void modifyStarshipSuccess() throws Exception {
        StarshipUpdateRequest request = new StarshipUpdateRequest(TEST_NAME, TEST_ID);
        MovieDtoNullable movie = MovieDtoFactory.create(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship = StarshipDtoFactory.create(1L, "Test Ship", movie);
        
        when(modifyStarshipPort.execute(TEST_ID, request)).thenReturn(starship);

        mockMvc.perform(put("/starships/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(TEST_ID))
               .andExpect(jsonPath("$.name").value(TEST_NAME))
               .andExpect(jsonPath("$.isNull").doesNotExist()); // Verify @JsonIgnore
    }

    @Test
    void modifyStarshipDuplicateName() throws Exception {
        StarshipUpdateRequest request = new StarshipUpdateRequest(TEST_NAME, TEST_ID);
        doThrow(new StarshipDuplicatedException(TEST_ID)).when(modifyStarshipPort).execute(TEST_ID, request);

        mockMvc.perform(put("/starships/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.title").value("Already exists"));
    }

    // POST /starships
    @Test
    void addStarshipSuccess() throws Exception {
        StarshipUpdateRequest request = new StarshipUpdateRequest(TEST_NAME, TEST_ID);
        MovieDtoNullable movie = MovieDtoFactory.create(1L, "Star Wars", 1986, false);
        StarshipDtoNullable starship = StarshipDtoFactory.create(1L, "X-wing", movie);
        
        when(addStarshipPort.execute(any())).thenReturn(starship);

        mockMvc.perform(post("/starships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(TEST_ID));
    }

    @Test
    void addStarshipValidationError() throws Exception {
        StarshipUpdateRequest invalidRequest = new StarshipUpdateRequest("", null); // Invalid fields

        mockMvc.perform(post("/starships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.name").value("must not be blank"))
               .andExpect(jsonPath("$.movieId").value("must not be null"));
    }

}