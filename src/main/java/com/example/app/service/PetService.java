package com.example.app.service;

import com.example.app.dto.PetDTO;
import com.example.app.dto.UserDTO;
import com.example.app.entity.Pet;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.mapper.PetMapper;
import com.example.app.repository.PetRepository;
import com.example.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.app.util.Messages.*;
import java.util.List;

/**
 * Business logic for Pet operations.
 */
@Slf4j
@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetService(PetRepository petRepository,
                      UserRepository userRepository,
                      PetMapper petMapper) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
    }

    @Transactional
    public PetDTO addPet(PetDTO petDTO) {
        log.info("Adding new pet: {}", petDTO);

        Pet pet = petMapper.toEntity(petDTO);

        if (pet.getOwner() != null && pet.getOwner().getId() != null) {
            Long ownerId = pet.getOwner().getId();
            User foundOwner = userRepository.findById(ownerId)
                    .orElseThrow(() -> {
                        log.error("Owner not found with id: {}", ownerId);
                        return new NotFoundException(OWNER_NOT_FOUND + ownerId);
                    });
            pet.setOwner(foundOwner);
            log.debug("Associated pet with owner id: {}", ownerId);
        }

        Pet saved = petRepository.save(pet);
        PetDTO result = petMapper.toDto(saved);
        log.info("Pet saved successfully with id: {}", result.getId());
        return result;
    }

    @Transactional
    public PetDTO updatePet(Long id, PetDTO patchDTO) {
        log.info("Updating pet with id: {}", id);

        Pet existing = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pet not found with id: {}", id);
                    return new NotFoundException(PET_NOT_FOUND + id);
                });

        // Update fields using MapStruct
        petMapper.updatePetFromDto(patchDTO, existing);

        if (patchDTO.getOwner() != null) {
            UserDTO user = patchDTO.getOwner();
            User owner = userRepository.findById(user.getId())
                    .orElseThrow(() -> {
                        log.error("Owner not found with id: {}", user.getId());
                return new NotFoundException(OWNER_NOT_FOUND + user.getId());
                    });
            existing.setOwner(owner);
        }

        Pet updated = petRepository.save(existing);
        PetDTO result = petMapper.toDto(updated);
        log.info("Pet with id {} updated successfully", id);
        return result;
    }

    @Transactional
    public void markPetDead(Long id) {
        log.warn("Marking pet with id {} as dead", id);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pet not found with id: {}", id);
                    return new NotFoundException(PET_NOT_FOUND + id);

                });
        pet.setAlive(false);
        petRepository.save(pet);
        log.info("Pet with id {} marked as dead successfully", id);
    }

    public List<PetDTO> findPetsByUser(String firstName, String lastName) {
        log.info("Finding pets by owner name: {} {}", firstName, lastName);
        List<Pet> pets = petRepository.findByOwner_FirstNameAndOwner_LastNameAndAliveTrue(firstName, lastName);
        List<PetDTO> results = petMapper.toDtoList(pets);
        log.info("Found {} pets for owner {} {}", results.size(), firstName, lastName);
        return results;
    }

    public List<PetDTO> findPetsByCity(String city) {
        log.info("Finding pets in city: {}", city);
        List<Pet> pets = petRepository.findByOwner_Address_CityIgnoreCaseAndAliveTrue(city);
        List<PetDTO> results = petMapper.toDtoList(pets);
        log.info("Found {} pets in city {}", results.size(), city);
        return results;
    }

    public List<PetDTO> findPetsByTypeAndCity(String type, String city) {
        log.info("Finding pets of type '{}' in city: {}", type, city);
        List<Pet> pets = petRepository.findByTypeIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue(type, city);
        List<PetDTO> results = petMapper.toDtoList(pets);
        log.info("Found {} pets of type '{}' in city {}", results.size(), type, city);
        return results;
    }

    public List<PetDTO> findPetsByGenderAndCity(String gender, String city) {
        log.info("Finding pets with owner gender '{}' in city: {}", gender, city);
        List<Pet> pets = petRepository.findByOwner_GenderIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue(gender, city);
        List<PetDTO> results = petMapper.toDtoList(pets);
        log.info("Found {} pets with owner gender '{}' in city {}", results.size(), gender, city);
        return results;
    }
}
