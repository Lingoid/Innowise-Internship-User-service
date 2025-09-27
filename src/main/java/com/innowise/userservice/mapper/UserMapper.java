package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(source = "birthDate", target = "birthDate", dateFormat = "yyyy-MM-dd")
    UserDTO fromEntity(User user);

    @Mapping(source = "birthDate", target = "birthDate", dateFormat = "yyyy-MM-dd")
    User toEntity(UserDTO userDTO);
}
