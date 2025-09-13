package com.example.app.service;

import com.example.app.entity.Address;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);

        address = Address.builder()
                .city("Paris")
                .type("Street")
                .addressName("Antoine Lavoisier")
                .number("10")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .age(30)
                .gender("female")
                .address(address)
                .alive(true)
                .build();
    }

    @Test
    void testAddUser() {
        when(userRepository.save(user)).thenReturn(user);
        User saved = userService.addUser(user);
        assertEquals("Jane", saved.getFirstName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        User patch = User.builder()
                .firstName("Janet")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(1L, patch);
        assertEquals("Janet", updated.getFirstName());
        assertEquals("Doe", updated.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        User patch = User.builder()
                .firstName("Janet")
                .build();
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(2L, patch));
    }

    @Test
    void testMarkUserDead() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.markUserDead(1L);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertFalse(captor.getValue().isAlive());
    }

    @Test
    void testMarkUserDeadNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.markUserDead(2L));
    }

    @Test
    void testFindHomonyms() {
        when(userRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(List.of(user));
        List<User> result = userService.findHomonyms("Jane", "Doe");
        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).getFirstName());
    }

    @Test
    void testFindWomenByCity() {
        when(userRepository.findByGenderAndAddress_CityIgnoreCase("female", "Paris")).thenReturn(List.of(user));
        List<User> result = userService.findWomenByCity("Paris");
        assertEquals(1, result.size());
        assertEquals("female", result.get(0).getGender());
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findById(1L);
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(2L));
    }
}
