package com.example.app.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Pet entity.
 *
 * Note:
 * - Single owner per Pet record.
 * - Multiple owners for the same physical pet would require a join entity (Ownership).
 * - Current design allows multiple users to share the same Address.
 */
@Entity
@Table(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    private boolean alive = true;

    /**
     * Convenience method to assign an owner
     */
    public void assignOwner(User user) {
        this.owner = user;
        if (user != null && !user.getPets().contains(this)) {
            user.getPets().add(this);
        }
    }
}
