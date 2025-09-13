package com.example.app.service;

import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for User operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User patch) {
        User existing = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found " + id));
        if (patch.getFirstName() != null) existing.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null) existing.setLastName(patch.getLastName());
        if (patch.getAge() != null) existing.setAge(patch.getAge());
        if (patch.getGender() != null) existing.setGender(patch.getGender());
        if (patch.getAddress() != null) existing.setAddress(patch.getAddress());
        return userRepository.save(existing);
    }

    @Transactional
    public void markUserDead(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found " + id));
        u.setAlive(false);
        userRepository.save(u);
    }

    public List<User> findHomonyms(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<User> findWomenByCity(String city) {
        return userRepository.findByGenderAndAddress_CityIgnoreCase("female", city);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found " + id));
    }
}
