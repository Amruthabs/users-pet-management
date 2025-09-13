package com.example.app.controller;

import com.example.app.entity.Pet;
import com.example.app.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pet REST APIs.
 */
@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) { this.petService = petService; }

    @Operation(summary = "Add a new pet (owner.id may be provided)")
    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        return ResponseEntity.ok(petService.addPet(pet));
    }

    @Operation(summary = "Update a pet by id")
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet patch) {
        return ResponseEntity.ok(petService.updatePet(id, patch));
    }

    @Operation(summary = "Mark pet as dead (soft delete)")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        petService.markPetDead(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pets owned by a user (handles homonyms)")
    @GetMapping("/by-user")
    public ResponseEntity<List<Pet>> petsByUser(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok(petService.findPetsByUser(firstName, lastName));
    }

    @Operation(summary = "Pets from a specific city (owner's city)")
    @GetMapping("/by-city")
    public ResponseEntity<List<Pet>> petsByCity(@RequestParam String city) {
        return ResponseEntity.ok(petService.findPetsByCity(city));
    }

    @Operation(summary = "Pets by type and city")
    @GetMapping("/by-type-city")
    public ResponseEntity<List<Pet>> petsByTypeAndCity(@RequestParam String type, @RequestParam String city) {
        return ResponseEntity.ok(petService.findPetsByTypeAndCity(type, city));
    }

    @Operation(summary = "Pets owned by women in a city")
    @GetMapping("/by-gender-city")
    public ResponseEntity<List<Pet>> petsByGenderCity(@RequestParam String gender, @RequestParam String city) {
        return ResponseEntity.ok(petService.findPetsByGenderAndCity(gender, city));
    }
}
