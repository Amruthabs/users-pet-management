package com.example.app.mapper;

import com.example.app.dto.UserDTO;
import com.example.app.entity.User;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    // Update existing User from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDTO dto, @MappingTarget User target);

    // Convert DTO to entity
    User toEntity(UserDTO dto);

    // Convert entity to DTO
    UserDTO toDto(User entity);

    // Convert List<Entity> to List<DTO>
    List<UserDTO> toDtoList(List<User> users);

    // Convert List<DTO> to List<Entity>
    List<User> toEntityList(List<UserDTO> dtos);
}
