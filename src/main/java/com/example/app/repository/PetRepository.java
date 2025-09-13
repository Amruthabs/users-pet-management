package com.example.app.repository;

import com.example.app.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwner_FirstNameAndOwner_LastNameAndAliveTrue(String firstName, String lastName);

    List<Pet> findByOwner_Address_CityIgnoreCaseAndAliveTrue(String city);

    List<Pet> findByTypeIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue(String type, String city);

    List<Pet> findByOwner_GenderIgnoreCaseAndOwner_Address_CityIgnoreCaseAndAliveTrue(String gender, String city);
}
