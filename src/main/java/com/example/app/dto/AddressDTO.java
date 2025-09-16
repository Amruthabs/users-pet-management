package com.example.app.dto;

import lombok.Data;

/**
 * DTO for transferring address details.
 */
@Data
public class AddressDTO {
    private String city;
    private String type;
    private String addressName;
    private String number;
    
}
