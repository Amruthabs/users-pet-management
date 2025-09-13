package com.example.app.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Address entity used for Users' addresses.
 * Example formatted: "10, road Avenue, India"
 */
@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String type;       
    private String addressName;
    private String number;

    /**
     * Returns a formatted address string like "10, road Avenue, India"
     */
    public String formatted() {
        StringBuilder sb = new StringBuilder();
        if (number != null && !number.isBlank()) sb.append(number).append(", ");
        if (type != null) sb.append(type).append(" ");
        if (addressName != null) sb.append(addressName);
        if (city != null) sb.append(", ").append(city);
        return sb.toString();
    }
}
