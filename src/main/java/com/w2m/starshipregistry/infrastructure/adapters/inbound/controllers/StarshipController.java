package com.w2m.starshipregistry.infrastructure.adapters.inbound.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDto;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.ports.inbound.AddStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.DeleteStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.FindAllStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.FindByIdStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.FindByNameStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipPort;
import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/starships")
@Tag(name = "Starship API", description = "Endpoints for managing starships")
public class StarshipController {

    private final FindAllStarshipPort findAllStarshipPort;
    private final FindByIdStarshipPort findByIdStarshipPort;
    private final FindByNameStarshipPort findByNameStarshipPort;
    private final DeleteStarshipPort deleteStarshipPort;
    private final ModifyStarshipPort modifyStarshipPort;
    private final AddStarshipPort addStarshipPort;

    // Scenario: Successfully find a starship by ID
    @GetMapping("/{id}")
    @Operation(summary = "Find a starship by ID", description = "Returns a single starship by its ID")
    @ApiResponse(responseCode = "200", description = "Starship found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StarshipDto.class)))
    @ApiResponse(responseCode = "404", description = "Starship not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<StarshipDtoNullable> getStarshipById(@PathVariable Long id) {
        return ResponseEntity.ok(findByIdStarshipPort.execute(id));

    }

    // Scenario: Search for starships by name
    @GetMapping("/search")
    @Operation(summary = "Search starships by name", description = "Returns a list of starships matching the name")
    @ApiResponse(responseCode = "200", description = "Search results", content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<StarshipDtoNullable>> searchStarshipsByName(@RequestParam String name) {
        List<StarshipDtoNullable> starships = findByNameStarshipPort.execute(name);
        return ResponseEntity.ok(starships);
    }

    // Scenario: Retrieve all starships with pagination
    @GetMapping
    @Operation(summary = "Retrieve all starships with pagination", description = "Returns paginated starships")
    @ApiResponse(responseCode = "200", description = "Paginated starships", content = @Content(mediaType = "application/json"))
    public ResponseEntity<PageResponse<StarshipDtoNullable>> getAllStarships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StarshipDtoNullable> starshipPage = findAllStarshipPort.execute(pageable);
        PageResponse<StarshipDtoNullable> response = PageResponse.from(starshipPage);
        return ResponseEntity.ok(response);
    }

    // Scenario: Successfully remove an existing starship
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a starship by ID", description = "Deletes a starship and clears related caches")
    @ApiResponse(responseCode = "204", description = "Starship removed successfully")
    @ApiResponse(responseCode = "404", description = "Starship not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Starship cannot be deleted due to dependencies", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteStarship(@PathVariable Long id) {

        deleteStarshipPort.execute(id);
        return ResponseEntity.noContent().build(); // 204 No Content

    }

    // Scenario: Successfully modify an existing starship
    @PutMapping("/{id}")
    @Operation(summary = "Modify a starship by ID", description = "Updates the details of an existing starship and clears related caches")
    @ApiResponse(responseCode = "200", description = "Starship updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StarshipDto.class)))
    @ApiResponse(responseCode = "404", description = "Starship not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Starship with the same name already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<StarshipDtoNullable> modifyStarship(
            @PathVariable Long id,
            @RequestBody StarshipUpdateRequest updateRequest) {

        StarshipDtoNullable updatedStarship = modifyStarshipPort.execute(id, updateRequest);
        return ResponseEntity.ok(updatedStarship); // 200 OK

    }

    // Scenario: add a new starship
    @PostMapping
    @Operation(summary = "Add a new starship", description = "Adds a new starship to the system and clears related caches")
    @ApiResponse(responseCode = "201", description = "Starship added successfully", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409", description = "Starship with the same name already exists", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<StarshipDtoNullable> addStarship(@Valid @RequestBody StarshipAddRequest addStarshipRequest) {
        StarshipDtoNullable result = addStarshipPort.execute(addStarshipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result); // 201 Created

    }
}
