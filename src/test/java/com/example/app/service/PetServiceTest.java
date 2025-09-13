package com.example.app.service;

import com.example.app.entity.Pet;
import com.example.app.entity.User;
import com.example.app.repository.PetRepository;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {

    @Test
    void markPetDead() {
        PetRepository petRepo = mock(PetRepository.class);
        UserRepository userRepo = mock(UserRepository.class);
        PetService service = new PetService(petRepo, userRepo);

        Pet p = new Pet("Chintu", 5, "dog", new User());
        p.setId(1L);
        when(petRepo.findById(1L)).thenReturn(Optional.of(p));
        service.markPetDead(1L);
        verify(petRepo, times(1)).save(p);
        assertFalse(p.isAlive());
    }
}
