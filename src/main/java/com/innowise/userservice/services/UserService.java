package com.innowise.userservice.services;

import com.innowise.userservice.dao.UserDAO;
import com.innowise.userservice.model.User;
import com.innowise.userservice.util.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(User user) {
        userDAO.createUser(user);
        return user;
    }

    public User getUserById(Long id) {
        return Optional.ofNullable(userDAO.getUserById(id))
                .orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEmail(String email) {
        return Optional.ofNullable(userDAO.getUserByEmail(email))
                .orElseThrow(UserNotFoundException::new);
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userDAO.getUsersByIds(ids);
    }

    @Transactional
    public User updateUser(User user) {
        Optional.ofNullable(userDAO.getUserById(user.getId()))
                .orElseThrow(UserNotFoundException::new);
        userDAO.updateUserById(user);
        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional.ofNullable(userDAO.getUserById(id))
                .orElseThrow(UserNotFoundException::new);
        userDAO.deleteUserById(id);
    }
}
