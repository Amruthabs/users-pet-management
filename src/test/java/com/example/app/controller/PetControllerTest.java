package com.example.app.controller;

import com.example.app.entity.Pet;
import com.example.app.entity.User;
import com.example.app.entity.Address;
import com.example.app.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)  // Only loads the PetController and related MVC components
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetService petService;   // Mock service layer

    @Test
    void testAddPet() throws Exception {
        User owner = User.builder()
                .firstName("John")
                .lastName("Smith")
                .age(40)
                .gender("male")
                .address(Address.builder()
                        .city("London")
                        .type("street")
                        .addressName("Baker Street")
                        .number("221B")
                        .build())
                .pets(new ArrayList<>())
                .build();

        Pet pet = Pet.builder()
                .name("Fido")
                .type("dog")
                .age(3)
                .owner(owner)
                .alive(true)
                .build();

        // Mock the service to return the same pet when saving
        Mockito.when(petService.addPet(Mockito.any(Pet.class))).thenReturn(pet);

        String json = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
