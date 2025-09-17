package com.example.app.controller;

import com.example.app.dto.PetDTO;
import com.example.app.dto.UserDTO;
import com.example.app.service.PetService;
import com.example.app.service.UserService;
import com.example.app.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String CITY = "Melbourne";

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;
    private PetDTO petDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        petDTO = new PetDTO();
        petDTO.setId(10L);
        petDTO.setType("Dog");
        petDTO.setOwner(userDTO);
    }

    @Test
    void testAddUser() {
        when(userService.addUser(userDTO)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.addUser(userDTO);

        assertThat(response.getBody()).isEqualTo(userDTO);
        verify(userService, times(1)).addUser(userDTO);
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(1L, userDTO)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(1L, userDTO);

        assertThat(response.getBody()).isEqualTo(userDTO);
        verify(userService, times(1)).updateUser(1L, userDTO);
    }

    @Test
    void testFindByName() {
        List<UserDTO> users = List.of(userDTO);
        when(userService.findHomonyms("John", "Doe")).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.findByName("John", "Doe");

        assertThat(response.getBody()).isEqualTo(users);
        verify(userService, times(1)).findHomonyms("John", "Doe");
    }

    @Test
    void testWomenByCity() {
        List<UserDTO> users = List.of(userDTO);
        when(userService.findWomenByCity(CITY)).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.womenByCity(CITY);

        assertThat(response.getBody()).isEqualTo(users);
        verify(userService, times(1)).findWomenByCity(CITY);
    }

    @Test
    void testUsersByPetTypeAndCity() {
        List<PetDTO> pets = List.of(petDTO);
        when(petService.findPetsByTypeAndCity("Dog", CITY)).thenReturn(pets);
        when(userService.findById(1L)).thenReturn(userDTO);

        ResponseEntity<List<UserDTO>> response = userController.usersByPetTypeAndCity("Dog", CITY);

        assertThat(response.getBody()).containsExactly(userDTO);
        verify(petService, times(1)).findPetsByTypeAndCity("Dog", CITY);
        verify(userService, times(1)).findById(1L);
    }
}
