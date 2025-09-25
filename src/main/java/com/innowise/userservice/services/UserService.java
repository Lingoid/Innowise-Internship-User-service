package com.innowise.userservice.services;

import com.innowise.userservice.dao.UserDAO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.User;
import com.innowise.userservice.util.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UserService(UserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        userDAO.createUser(user);
        return userMapper.fromEntity(user);
    }

    public UserDTO getUserById(Long id) {
        User user = Optional.ofNullable(userDAO.getUserById(id))
                .orElseThrow(UserNotFoundException::new);
        return userMapper.fromEntity(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = Optional.ofNullable(userDAO.getUserByEmail(email))
                .orElseThrow(UserNotFoundException::new);
        return userMapper.fromEntity(user);
    }

    public List<UserDTO> getUsersByIds(List<Long> ids) {
        return userDAO.getUsersByIds(ids).stream()
                .map(userMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setId(id);
        Optional.ofNullable(userDAO.getUserById(user.getId()))
                .orElseThrow(UserNotFoundException::new);
        userDAO.updateUserById(user);
        return userMapper.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional.ofNullable(userDAO.getUserById(id))
                .orElseThrow(UserNotFoundException::new);
        userDAO.deleteUserById(id);
    }
}
