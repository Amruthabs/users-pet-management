package com.example.app.controller;

import com.example.app.entity.User;
import com.example.app.service.PetService;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User REST APIs.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PetService petService;

    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    @Operation(summary = "Add a new user")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @Operation(summary = "Update user by id")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User patch) {
        return ResponseEntity.ok(userService.updateUser(id, patch));
    }

    @Operation(summary = "Mark user as dead (soft delete)")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.markUserDead(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find homonyms (users by first and last name)")
    @GetMapping("/search")
    public ResponseEntity<List<User>> findByName(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok(userService.findHomonyms(firstName, lastName));
    }

    @Operation(summary = "Get women users in a city")
    @GetMapping("/women")
    public ResponseEntity<List<User>> womenByCity(@RequestParam String city) {
        return ResponseEntity.ok(userService.findWomenByCity(city));
    }

    @Operation(summary = "Find users that own a specific kind of pet in a city")
    @GetMapping("/by-pet-type")
    public ResponseEntity<List<User>> usersByPetTypeAndCity(@RequestParam String type, @RequestParam String city) {
        var pets = petService.findPetsByTypeAndCity(type, city);
        var owners = pets.stream().map(Pet -> Pet.getOwner()).distinct().toList();
        return ResponseEntity.ok(owners);
    }
}
