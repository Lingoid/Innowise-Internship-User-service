package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDTO fromEntity(User user);
    User toEntity(UserDTO userDTO);
}
