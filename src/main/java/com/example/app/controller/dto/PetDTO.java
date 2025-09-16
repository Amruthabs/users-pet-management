package com.example.app.dto;

import lombok.Data;

/**
 * DTO for partial pet updates.
 */
@Data
public class PetDTO {
    private Long id; 
    private String name;
    private Integer age;
    private String type;
    private UserDTO owner;
}
