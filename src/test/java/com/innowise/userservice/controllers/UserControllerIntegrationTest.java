package com.innowise.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.dao.UserDAO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@Transactional
class UserControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDAO.deleteAll();
        userDTO = TestDataFactory.createDefaultUserDTO();
    }

    private User saveDefaultUser() {
        return userDAO.createUser(TestDataFactory.createDefaultUser());
    }

    static class TestDataFactory {
        static UserDTO createDefaultUserDTO() {
            UserDTO dto = new UserDTO();
            dto.setName("Maksim");
            dto.setSurname("Bagel");
            dto.setEmail("maks2004@example.com");
            dto.setBirthDate(LocalDate.of(2004, 3, 15));
            return dto;
        }

        static User createDefaultUser() {
            return new User(null, "Maksim", "Bagel",
                    LocalDate.of(2004, 3, 15), "maks2004@example.com");
        }
    }

    @Test
    void createUser_returnsCreatedUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    void getUser_returnsUser() throws Exception {
        User savedUser = saveDefaultUser();

        mockMvc.perform(get("/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(savedUser.getName()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()));
    }

    @Test
    void getUsers_returnsListOfUsers() throws Exception {
        User savedUser = saveDefaultUser();

        mockMvc.perform(get("/users")
                        .param("ids", String.valueOf(savedUser.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(savedUser.getName()));
    }

    @Test
    void updateUser_updatesAndReturnsUser() throws Exception {
        User savedUser = saveDefaultUser();
        userDTO.setName("Alex");

        mockMvc.perform(put("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"));
    }

    @Test
    void deleteUser_removesUser() throws Exception {
        User savedUser = saveDefaultUser();

        mockMvc.perform(delete("/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUser_cachesResult() throws Exception {
        User savedUser = saveDefaultUser();

        mockMvc.perform(get("/users/{id}", savedUser.getId()))
                .andExpect(status().isOk());

        Object cached = cacheManager.getCache("UserCache").get(savedUser.getId()).get();

        objectMapper.findAndRegisterModules();
        UserDTO cachedUser = objectMapper.convertValue(cached, UserDTO.class);

        assertAll(() -> {
            assertNotNull(cachedUser);
            assertEquals(savedUser.getName(), cachedUser.getName());
            assertEquals(savedUser.getEmail(), cachedUser.getEmail());
            assertEquals(savedUser.getBirthDate(), cachedUser.getBirthDate());
        });
    }
}
