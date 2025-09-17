package com.example.app.service;

import com.example.app.dto.UserDTO;
import com.example.app.dto.UserDTO;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.mapper.UserMapper;
import com.example.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.app.util.Messages.*;

import java.util.List;

/**
 * Business logic for User operations.
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        log.info("Adding new user: {}", userDTO);
        User entity = userMapper.toEntity(userDTO);
        User saved = userRepository.save(entity);
        UserDTO result = userMapper.toDto(saved);
        log.info("User saved successfully with id: {}", result.getId());
        return result;
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        log.info("Updating user with id: {}", id);

        User existing = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ERROR_USER_NOT_FOUND, id);
                    return new NotFoundException(USER_NOT_FOUND + id);
                });

        userMapper.updateUserFromDto(dto, existing);
        User updated = userRepository.save(existing);

        UserDTO result = userMapper.toDto(updated);
        log.info("User with id {} updated successfully", id);
        return result;
    }

    @Transactional
    public void markUserDead(Long id) {
        log.warn("Marking user with id {} as dead", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ERROR_USER_NOT_FOUND, id);
                    return new NotFoundException(USER_NOT_FOUND + id);
                });
        u.setAlive(false);
        userRepository.save(u);
        log.info("User with id {} marked as dead successfully", id);
    }

    public List<UserDTO> findHomonyms(String firstName, String lastName) {
        log.info("Searching for users with firstName={} and lastName={}", firstName, lastName);
        List<User> users = userRepository.findByFirstNameAndLastName(firstName, lastName);
        List<UserDTO> results = userMapper.toDtoList(users);
        log.info("Found {} users with name {} {}", results.size(), firstName, lastName);
        return results;
    }

    public List<UserDTO> findWomenByCity(String city) {
        log.info("Searching for female users in city: {}", city);
        List<User> users = userRepository.findByGenderAndAddress_CityIgnoreCase("female", city);
        List<UserDTO> results = userMapper.toDtoList(users);
        log.info("Found {} female users in city {}", results.size(), city);
        return results;
    }

    public UserDTO findById(Long id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ERROR_USER_NOT_FOUND, id);
                    return new NotFoundException(USER_NOT_FOUND + id);
                });
        return userMapper.toDto(user);
    }
}
