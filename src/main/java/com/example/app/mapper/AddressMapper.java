package com.example.app.mapper;

import com.example.app.dto.AddressDTO;
import com.example.app.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDto(Address entity);
    Address toEntity(AddressDTO dto);
}
