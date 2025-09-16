package com.example.app.service;

import com.example.app.dto.PetDTO;
import com.example.app.dto.UserDTO;
import com.example.app.entity.Pet;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.mapper.PetMapper;
import com.example.app.repository.PetRepository;
import com.example.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;
import static com.example.app.util.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetService petService;

    private Pet pet;
    private PetDTO petDTO;
    private User owner;
    private UserDTO ownerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        owner.setId(1L);

        ownerDTO = new UserDTO();
        ownerDTO.setId(1L);

        pet = new Pet();
        pet.setId(10L);
        pet.setOwner(owner);
        pet.setAlive(true);

        petDTO = new PetDTO();
        petDTO.setId(10L);
        petDTO.setOwner(ownerDTO);
    }

    @Test
    void testAddPet_WithOwner() {
        when(petMapper.toEntity(petDTO)).thenReturn(pet);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(petRepository.save(pet)).thenReturn(pet);
        when(petMapper.toDto(pet)).thenReturn(petDTO);

        PetDTO result = petService.addPet(petDTO);

        assertNotNull(result);
        assertEquals(petDTO.getId(), result.getId());

        verify(petMapper).toEntity(petDTO);
        verify(userRepository).findById(owner.getId());
        verify(petRepository).save(pet);
        verify(petMapper).toDto(pet);
    }

    @Test
    void testAddPet_OwnerNotFound() {
        when(petMapper.toEntity(petDTO)).thenReturn(pet);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> petService.addPet(petDTO));
        assertEquals(OWNER_NOT_FOUND + owner.getId(), ex.getMessage());
    }

    @Test
    void testUpdatePet_WithOwner() {
        PetDTO patchDTO = new PetDTO();
        patchDTO.setOwner(ownerDTO);

        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(userRepository.findById(ownerDTO.getId())).thenReturn(Optional.of(owner));
        doNothing().when(petMapper).updatePetFromDto(patchDTO, pet);
        when(petRepository.save(pet)).thenReturn(pet);
        when(petMapper.toDto(pet)).thenReturn(petDTO);

        PetDTO result = petService.updatePet(pet.getId(), patchDTO);

        assertNotNull(result);
        verify(petMapper).updatePetFromDto(patchDTO, pet);
        verify(petRepository).save(pet);
        verify(petMapper).toDto(pet);
    }

    @Test
    void testUpdatePet_PetNotFound() {
        when(petRepository.findById(pet.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> petService.updatePet(pet.getId(), petDTO));
        assertEquals(PET_NOT_FOUND + pet.getId(), ex.getMessage());
    }

    @Test
    void testUpdatePet_OwnerNotFound() {
        PetDTO patchDTO = new PetDTO();
        patchDTO.setOwner(ownerDTO);

        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(userRepository.findById(ownerDTO.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> petService.updatePet(pet.getId(), patchDTO));
        assertEquals(OWNER_NOT_FOUND + ownerDTO.getId(), ex.getMessage());
    }

    @Test
    void testMarkPetDead() {
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(petRepository.save(pet)).thenReturn(pet);

        petService.markPetDead(pet.getId());

        assertFalse(pet.isAlive());
        verify(petRepository).save(pet);
    }

    @Test
    void testMarkPetDead_NotFound() {
        when(petRepository.findById(pet.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> petService.markPetDead(pet.getId()));
        assertEquals(PET_NOT_FOUND + pet.getId(), ex.getMessage());
    }

    @Test
    void testFindPetsByUser() {
        when(petRepository.findByOwner_FirstNameAndOwner_LastNameAndAliveTrue("John", "Doe")).thenReturn(List.of(pet));
        when(petMapper.toDtoList(List.of(pet))).thenReturn(List.of(petDTO));

        List<PetDTO> result = petService.findPetsByUser("John", "Doe");

        assertEquals(1, result.size());
        assertEquals(petDTO.getId(), result.get(0).getId());
    }

    @Test
    void testFindPetsByCity() {
        when(petRepository.findByOwner_Address_CityIgnoreCaseAndAliveTrue("CityA")).thenReturn(List.of(pet));
        when(petMapper.toDtoList(List.of(pet))).thenReturn(List.of(petDTO));

        List<PetDTO> result = petService.findPetsByCity("CityA");

        assertEquals(1, result.size());
        assertEquals(petDTO.getId(), result.get(0).getId());
    }

    @Test
    void testFindPetsByTypeAndCity() {
        when(petRepository.findByTypeIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue("Dog", "CityA"))
                .thenReturn(List.of(pet));
        when(petMapper.toDtoList(List.of(pet))).thenReturn(List.of(petDTO));

        List<PetDTO> result = petService.findPetsByTypeAndCity("Dog", "CityA");

        assertEquals(1, result.size());
        assertEquals(petDTO.getId(), result.get(0).getId());
    }

    @Test
    void testFindPetsByGenderAndCity() {
        when(petRepository.findByOwner_GenderIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue("female", "CityA"))
                .thenReturn(List.of(pet));
        when(petMapper.toDtoList(List.of(pet))).thenReturn(List.of(petDTO));

        List<PetDTO> result = petService.findPetsByGenderAndCity("female", "CityA");

        assertEquals(1, result.size());
        assertEquals(petDTO.getId(), result.get(0).getId());
    }
}
