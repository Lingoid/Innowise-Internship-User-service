package com.innowise.userservice.services;

import com.innowise.userservice.dao.UserDAO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.User;
import com.innowise.userservice.util.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setName("Maksim");
        user.setEmail("maks2004@example.com");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Maksim");
        userDTO.setEmail("maks2004@example.com");
    }


    @Test
    void createUser() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userMapper.fromEntity(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals("Maksim", result.getName());
        });

        verify(userDAO).createUser(user);
        verify(userMapper).fromEntity(user);
        verify(userMapper).toEntity(userDTO);
    }

    @Test
    void getUserById_returnsUserDTO_WhenUserExists() {
        when(userDAO.getUserById(1L)).thenReturn(user);
        when(userMapper.fromEntity(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals("Maksim", result.getName());
        });

        verify(userDAO).getUserById(1L);
        verify(userMapper).fromEntity(user);
    }

    @Test
    void getUserById_throwsException_whenUserNotFound() {
        when(userDAO.getUserById(1L)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userDAO).getUserById(1L);
    }

    @Test
    void getUserByEmail_returnsUserDTO_whenUserExists() {
        when(userDAO.getUserByEmail("maks2004@example.com")).thenReturn(user);
        when(userMapper.fromEntity(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByEmail("maks2004@example.com");

        assertEquals("Maksim", result.getName());
        verify(userDAO).getUserByEmail("maks2004@example.com");
    }

    @Test
    void getUserByEmail_throwsException_whenUserNotFound() {
        when(userDAO.getUserByEmail("blablabla@example.com")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("blablabla@example.com"));
        verify(userDAO).getUserByEmail("blablabla@example.com");
    }

    @Test
    void getUsersByIds_returnsListOfUserDTOs() {
        when(userDAO.getUsersByIds(Mockito.anyList())).thenReturn(List.of(user));
        when(userMapper.fromEntity(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getUsersByIds(List.of(1L));

        assertAll(() -> {
            assertEquals(1, result.size());
            assertEquals("Maksim", result.getFirst().getName());
        });

        verify(userDAO).getUsersByIds(List.of(1L));
        verify(userMapper).fromEntity(user);
    }

    @Test
    void updateUser_updatesAndReturnsDTO_whenUserExists() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userDAO.getUserById(1L)).thenReturn(user);
        when(userMapper.fromEntity(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertEquals("Maksim", result.getName());
        verify(userDAO).getUserById(1L);
        verify(userDAO).updateUserById(user);
        verify(userMapper).toEntity(userDTO);
        verify(userMapper).fromEntity(user);
    }

    @Test
    void updateUser_throwsException_whenUserNotFound() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userDAO.getUserById(1L)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userDTO));
        verify(userDAO).getUserById(1L);
    }

    @Test
    void deleteUser_deletes_whenUserExists() {
        when(userDAO.getUserById(1L)).thenReturn(user);

        userService.deleteUser(1L);

        verify(userDAO).getUserById(1L);
        verify(userDAO).deleteUserById(1L);
    }

    @Test
    void deleteUser_throwsException_whenUserNotFound() {
        when(userDAO.getUserById(1L)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userDAO).getUserById(1L);
    }

}