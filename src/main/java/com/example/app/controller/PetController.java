package com.example.app.controller;

import com.example.app.dto.PetDTO;
import com.example.app.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Pet REST APIs.
 */
@Slf4j
@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(summary = "Add a new pet (owner.id may be provided)")
    @PostMapping
    public ResponseEntity<PetDTO> addPet(@RequestBody PetDTO dto) {
        log.info("Request to add pet: {}", dto);
        PetDTO saved = petService.addPet(dto);
        log.info("Pet added successfully with id: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update a pet by id")
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Long id, @RequestBody PetDTO dto) {
        log.info("Request to update pet with id: {}, dto: {}", id, dto);
        PetDTO updated = petService.updatePet(id, dto);
        log.info("Pet updated successfully: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Mark pet as dead (soft delete)")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        log.warn("Request to deactivate pet with id: {}", id);
        petService.markPetDead(id);
        log.info("Pet with id {} marked as dead", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pets owned by a user (handles homonyms)")
    @GetMapping("/by-user")
    public ResponseEntity<List<PetDTO>> petsByUser(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        log.info("Fetching pets by user: {} {}", firstName, lastName);
        List<PetDTO> pets = petService.findPetsByUser(firstName, lastName);
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Pets from a specific city (owner's city)")
    @GetMapping("/by-city")
    public ResponseEntity<List<PetDTO>> petsByCity(@RequestParam String city) {
        log.info("Fetching pets by city: {}", city);
        List<PetDTO> pets = petService.findPetsByCity(city);
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Pets by type and city")
    @GetMapping("/by-type-city")
    public ResponseEntity<List<PetDTO>> petsByTypeAndCity(
            @RequestParam String type,
            @RequestParam String city) {
        log.info("Fetching pets by type: {} and city: {}", type, city);
        List<PetDTO> pets = petService.findPetsByTypeAndCity(type, city);
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Pets owned by women in a city")
    @GetMapping("/by-gender-city")
    public ResponseEntity<List<PetDTO>> petsByGenderCity(
            @RequestParam String gender,
            @RequestParam String city) {
        log.info("Fetching pets by owner gender: {} and city: {}", gender, city);
        List<PetDTO> pets = petService.findPetsByGenderAndCity(gender, city);
        return ResponseEntity.ok(pets);
    }
}
