package com.example.app.service;

import com.example.app.entity.Pet;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.PetRepository;
import com.example.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for Pet operations.
 */
@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Pet addPet(Pet pet) {
        if (pet.getOwner() != null && pet.getOwner().getId() != null) {
            Long ownerId = pet.getOwner().getId();
            User foundOwner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new NotFoundException("Owner not found " + ownerId));
            pet.setOwner(foundOwner);
        }
        return petRepository.save(pet);
    }

    @Transactional
    public Pet updatePet(Long id, Pet patch) {
        Pet existing = petRepository.findById(id).orElseThrow(() -> new NotFoundException("Pet not found " + id));
        if (patch.getName() != null) existing.setName(patch.getName());
        if (patch.getAge() != null) existing.setAge(patch.getAge());
        if (patch.getType() != null) existing.setType(patch.getType());
        if (patch.getOwner() != null && patch.getOwner().getId() != null) {
            User owner = userRepository.findById(patch.getOwner().getId()).orElseThrow(() -> new NotFoundException("Owner not found " + patch.getOwner().getId()));
            existing.setOwner(owner);
        }
        return petRepository.save(existing);
    }

    @Transactional
    public void markPetDead(Long id) {
        Pet p = petRepository.findById(id).orElseThrow(() -> new NotFoundException("Pet not found " + id));
        p.setAlive(false);
        petRepository.save(p);
    }

    public List<Pet> findPetsByUser(String firstName, String lastName) {
        return petRepository.findByOwner_FirstNameAndOwner_LastNameAndAliveTrue(firstName, lastName);
    }

    public List<Pet> findPetsByCity(String city) {
        return petRepository.findByOwner_Address_CityIgnoreCaseAndAliveTrue(city);
    }

    public List<Pet> findPetsByTypeAndCity(String type, String city) {
        return petRepository.findByTypeIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue(type, city);
    }

    public List<Pet> findPetsByGenderAndCity(String gender, String city) {
        return petRepository.findByOwner_GenderIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue(gender, city);
    }
}
