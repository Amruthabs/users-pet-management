package com.example.app.service;

import com.example.app.dto.UserDTO;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.mapper.UserMapper;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static com.example.app.util.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setAlive(true);

        userDTO = new UserDTO();
        userDTO.setId(1L);
    }

    @Test
    void testAddUser() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.addUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());

        verify(userMapper).toEntity(userDTO);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void testUpdateUserSuccess() {
        UserDTO patchDTO = new UserDTO();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(patchDTO, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(user.getId(), patchDTO);

        assertNotNull(result);
        verify(userMapper).updateUserFromDto(patchDTO, user);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void testUpdateUserUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.updateUser(user.getId(), userDTO));
        assertEquals(USER_NOT_FOUND + user.getId(), ex.getMessage());
    }

    @Test
    void testMarkUserDeadSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.markUserDead(user.getId());

        assertFalse(user.isAlive());
        verify(userRepository).save(user);
    }

    @Test
    void testMarkUserDeadUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.markUserDead(user.getId()));
        assertEquals(USER_NOT_FOUND + user.getId(), ex.getMessage());
    }

    @Test
    void testFindHomonyms() {
        List<User> users = List.of(user);
        List<UserDTO> dtos = List.of(userDTO);

        when(userRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        List<UserDTO> result = userService.findHomonyms("John", "Doe");

        assertEquals(1, result.size());
        assertEquals(userDTO.getId(), result.get(0).getId());
    }

    @Test
    void testFindWomenByCity() {
        List<User> users = List.of(user);
        List<UserDTO> dtos = List.of(userDTO);

        when(userRepository.findByGenderAndAddress_CityIgnoreCase("female", "CityA")).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        List<UserDTO> result = userService.findWomenByCity("CityA");

        assertEquals(1, result.size());
        assertEquals(userDTO.getId(), result.get(0).getId());
    }

    @Test
    void testFindByIdSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(user.getId());

        assertEquals(userDTO.getId(), result.getId());
    }

    @Test
    void testFindByIdUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.findById(user.getId()));
        assertEquals(USER_NOT_FOUND + user.getId(), ex.getMessage());
    }
}
