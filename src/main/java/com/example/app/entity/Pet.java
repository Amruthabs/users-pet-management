package com.example.app.entity;

import jakarta.persistence.*;

/**
 * Pet entity.
 *
 * Note: model assumes single owner per Pet record. To model same physical pet shared by multiple owners,
 * an Ownership join entity would be required. Current design supports multiple owners at same address
 * by letting multiple User instances share the same Address.
 */
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private boolean alive = true;

    public Pet() {}

    public Pet(String name, Integer age, String type, User owner) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.owner = owner;
        this.alive = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}
