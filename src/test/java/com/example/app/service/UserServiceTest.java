package com.example.app.service;

import com.example.app.entity.Address;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void addAndMarkDead() {
        UserRepository repo = mock(UserRepository.class);
        UserService service = new UserService(repo);

        User u = new User("Mridivika", "Am", 30, "female", new Address("India","road","HCL","10"));
        when(repo.save(any())).thenReturn(u);
        User saved = service.addUser(u);
        assertEquals("Mridivika", saved.getFirstName());

        User persisted = new User("XYZ","Sc",40,"male", null);
        persisted.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(persisted));
        service.markUserDead(1L);
        verify(repo, times(1)).save(persisted);
        assertFalse(persisted.isAlive());
    }
}
