package com.example.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String city;

    private String type;
    private String addressName;
    private String number;

    public String formatted() {
        StringBuilder sb = new StringBuilder();
        if (number != null && !number.isBlank()) sb.append(number).append(", ");
        if (type != null) sb.append(type).append(" ");
        if (addressName != null) sb.append(addressName);
        if (city != null) sb.append(", ").append(city);
        return sb.toString();
    }


}
