package com.example.app.dto;

import lombok.Data;

/**
 * DTO for partial user updates.
 */
@Data
public class UserDTO {
    private Long id; 
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private AddressDTO address;
}
