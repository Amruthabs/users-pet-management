package com.example.app.controller;

import com.example.app.dto.UserDTO;
import com.example.app.dto.PetDTO;
import java.util.Objects;
import com.example.app.service.PetService;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User REST APIs.
 */
@Slf4j
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
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO dto) {
        log.info("Request to add user: {}", dto);
        UserDTO userDtoSaved = userService.addUser(dto);
        log.info("User added successfully with id: {}", userDtoSaved.getId());
        return ResponseEntity.ok(userDtoSaved);
    }

    @Operation(summary = "Update user by id")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        log.info("Request to update user with id: {}, dto: {}", id, dto);
        UserDTO userDtoUpdated = userService.updateUser(id, dto);
        log.info("User updated successfully: {}", userDtoUpdated);
        return ResponseEntity.ok(userDtoUpdated);
    }

    @Operation(summary = "Find homonyms (users by first and last name)")
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> findByName(@RequestParam String firstName,
                                                    @RequestParam String lastName) {
        log.info("Fetching users by name: {} {}", firstName, lastName);
        List<UserDTO> userDtoList = userService.findHomonyms(firstName, lastName);
        return ResponseEntity.ok(userDtoList);
    }

    @Operation(summary = "Get women users in a city")
    @GetMapping("/women")
    public ResponseEntity<List<UserDTO>> womenByCity(@RequestParam String city) {
        log.info("Fetching women users in city: {}", city);
        List<UserDTO> userDtoList = userService.findWomenByCity(city);
        return ResponseEntity.ok(userDtoList);
    }

    @Operation(summary = "Find users that own a specific kind of pet in a city")
    @GetMapping("/by-pet-type")
    public ResponseEntity<List<UserDTO>> usersByPetTypeAndCity(@RequestParam String type,
                                                            @RequestParam String city) {
        log.info("Fetching users by pet type: {} and city: {}", type, city);
        List<PetDTO> petDtoList = petService.findPetsByTypeAndCity(type, city);

       List<UserDTO> ownerDtoList = petDtoList.stream()
        .map(petDto -> userService.findById(petDto.getOwner().getId()))  
        .distinct()
        .toList();


        log.info("Found {} owners with pets of type {} in city {}", ownerDtoList.size(), type, city);
        return ResponseEntity.ok(ownerDtoList);
    }
}
