package com.example.app.repository;

import com.example.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    List<User> findByGenderAndAddress_CityIgnoreCase(String gender, String city);
}
