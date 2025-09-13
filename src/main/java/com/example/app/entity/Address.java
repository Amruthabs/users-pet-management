package com.example.app.entity;

import jakarta.persistence.*;

/**
 * Address entity used for Users' addresses.
 * Example formatted: "10, road Avenue, India"
 */
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String type;       
    private String addressName;
    private String number;

    public Address() {}

    public Address(String city, String type, String addressName, String number) {
        this.city = city;
        this.type = type;
        this.addressName = addressName;
        this.number = number;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAddressName() { return addressName; }
    public void setAddressName(String addressName) { this.addressName = addressName; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String formatted() {
        StringBuilder sb = new StringBuilder();
        if (number != null && !number.isBlank()) sb.append(number).append(", ");
        if (type != null) sb.append(type).append(" ");
        if (addressName != null) sb.append(addressName);
        if (city != null) sb.append(", ").append(city);
        return sb.toString();
    }
}
