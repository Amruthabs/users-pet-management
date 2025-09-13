package com.example.app.service;

import com.example.app.entity.Pet;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.PetRepository;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetService petService;

    private User user;
    private Pet pet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).firstName("John").lastName("Doe").alive(true).build();
        pet = Pet.builder().id(1L).name("Doggo").owner(user).alive(true).build();
    }

    @Test
    void testSavePetWithOwner() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(petRepository.save(pet)).thenReturn(pet);
        Pet saved = petService.addPet(pet);
        assertEquals("Doggo", saved.getName());
        assertEquals(user, saved.getOwner());
    }

    @Test
    void testSavePetOwnerNotFound() {
        pet.getOwner().setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> petService.addPet(pet));
    }

    

    @Test
    void testDeletePet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        petService.markPetDead(1L);
        verify(petRepository).save(argThat(p -> !p.isAlive()));
    }
}
