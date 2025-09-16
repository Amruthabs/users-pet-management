package com.example.app.mapper;

import com.example.app.dto.PetDTO;
import com.example.app.entity.Pet;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PetMapper {

    // Update existing Pet entity from a DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePetFromDto(PetDTO dto, @MappingTarget Pet target);

    // Convert DTO to entity
    Pet toEntity(PetDTO dto);

    // Convert entity to DTO
    PetDTO toDto(Pet entity);
    List<PetDTO> toDtoList(List<Pet> entities);   
    List<Pet> toEntityList(List<PetDTO> dtos); 
}
