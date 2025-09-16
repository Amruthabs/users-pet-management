package com.example.app.controller;

import com.example.app.dto.PetDTO;
import com.example.app.service.PetService;
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
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private PetDTO petDTO;

    @BeforeEach
    void setUp() {
        petDTO = new PetDTO();
        petDTO.setId(1L);
        petDTO.setType("Dog");
    }

    @Test
    void testAddPet() {
        when(petService.addPet(any(PetDTO.class))).thenReturn(petDTO);

        ResponseEntity<PetDTO> response = petController.addPet(petDTO);

        assertThat(response.getBody()).isEqualTo(petDTO);
        verify(petService, times(1)).addPet(petDTO);
    }

    @Test
    void testUpdatePet() {
        when(petService.updatePet(eq(1L), any(PetDTO.class))).thenReturn(petDTO);

        ResponseEntity<PetDTO> response = petController.updatePet(1L, petDTO);

        assertThat(response.getBody()).isEqualTo(petDTO);
        verify(petService, times(1)).updatePet(1L, petDTO);
    }

    @Test
    void testDeactivate() {
        doNothing().when(petService).markPetDead(1L);

        ResponseEntity<Void> response = petController.deactivate(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(petService, times(1)).markPetDead(1L);
    }

    @Test
    void testPetsByUser() {
        List<PetDTO> list = List.of(petDTO);
        when(petService.findPetsByUser("John", "Doe")).thenReturn(list);

        ResponseEntity<List<PetDTO>> response = petController.petsByUser("John", "Doe");

        assertThat(response.getBody()).isEqualTo(list);
        verify(petService, times(1)).findPetsByUser("John", "Doe");
    }

    @Test
    void testPetsByCity() {
        List<PetDTO> list = List.of(petDTO);
        when(petService.findPetsByCity("Melbourne")).thenReturn(list);

        ResponseEntity<List<PetDTO>> response = petController.petsByCity("Melbourne");

        assertThat(response.getBody()).isEqualTo(list);
        verify(petService, times(1)).findPetsByCity("Melbourne");
    }

    @Test
    void testPetsByTypeAndCity() {
        List<PetDTO> list = List.of(petDTO);
        when(petService.findPetsByTypeAndCity("Dog", "Melbourne")).thenReturn(list);

        ResponseEntity<List<PetDTO>> response = petController.petsByTypeAndCity("Dog", "Melbourne");

        assertThat(response.getBody()).isEqualTo(list);
        verify(petService, times(1)).findPetsByTypeAndCity("Dog", "Melbourne");
    }

    @Test
    void testPetsByGenderCity() {
        List<PetDTO> list = List.of(petDTO);
        when(petService.findPetsByGenderAndCity("female", "Melbourne")).thenReturn(list);

        ResponseEntity<List<PetDTO>> response = petController.petsByGenderCity("female", "Melbourne");

        assertThat(response.getBody()).isEqualTo(list);
        verify(petService, times(1)).findPetsByGenderAndCity("female", "Melbourne");
    }
}
