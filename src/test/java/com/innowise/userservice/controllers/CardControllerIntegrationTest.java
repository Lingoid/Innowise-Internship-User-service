package com.innowise.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.dao.CardDAO;
import com.innowise.userservice.dao.UserDAO;
import com.innowise.userservice.dto.CardDTO;
import com.innowise.userservice.model.Card;
import com.innowise.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@Transactional
class CardControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Autowired
    private CardDAO cardDAO;

    private CardDTO cardDTO;

    @Autowired
    private UserDAO userDAO;


    @BeforeEach
    void setUp() {
        cardDAO.deleteAll();

        User savedUser = userDAO.createUser(
                new User(null, "Test User","Bagel", LocalDate.of(2000,1,1), "test@example.com")
        );

        cardDTO = TestDataFactory.createDefaultCardDTO(savedUser.getId());
    }

    private Card saveDefaultCard() {
        Card card = TestDataFactory.createDefaultCard(cardDTO.getUserId());
        return cardDAO.createCard(card);
    }

    static class TestDataFactory {

        static CardDTO createDefaultCardDTO(Long userId) {
            CardDTO dto = new CardDTO();
            dto.setUserId(userId);
            dto.setNumber("123456789012");
            dto.setHolder("Maksim Bagel");
            dto.setExpirationDate(LocalDate.of(2030, 12, 31));
            return dto;
        }

        static Card createDefaultCard(Long userId) {
            return new Card(null, userId, "123456789012", "Maksim Bagel",
                    LocalDate.of(2030, 12, 31));
        }

        static User createDefaultUser() {
            return new User(null, "Test User", "Bagel", LocalDate.of(2000,1,1), "test@example.com");
        }
    }

    @Test
    void createCard_returnsCreatedCard() throws Exception {
        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(cardDTO.getNumber()))
                .andExpect(jsonPath("$.holder").value(cardDTO.getHolder()));
    }

    @Test
    void getCard_returnsCard() throws Exception {
        Card savedCard = saveDefaultCard();

        mockMvc.perform(get("/cards/{id}", savedCard.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(savedCard.getNumber()))
                .andExpect(jsonPath("$.holder").value(savedCard.getHolder()));
    }

    @Test
    void getCards_returnsListOfCards() throws Exception {
        Card savedCard = saveDefaultCard();

        mockMvc.perform(get("/cards")
                        .param("ids", String.valueOf(savedCard.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(savedCard.getNumber()));
    }

    @Test
    void updateCard_updatesAndReturnsCard() throws Exception {
        Card savedCard = saveDefaultCard();
        cardDTO.setHolder("Alex Bagel");

        mockMvc.perform(put("/cards/{id}", savedCard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value("Alex Bagel"));
    }

    @Test
    void deleteCard_removesCard() throws Exception {
        Card savedCard = saveDefaultCard();

        mockMvc.perform(delete("/cards/{id}", savedCard.getId()))
                .andExpect(status().isNoContent());
    }
    @Test
    void getCard_cachesResult() throws Exception {
        Card savedCard = saveDefaultCard();

        mockMvc.perform(get("/cards/{id}", savedCard.getId()))
                .andExpect(status().isOk());

        Object cached = cacheManager.getCache("CardCache").get(savedCard.getId()).get();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        CardDTO cachedCard = mapper.convertValue(cached, CardDTO.class);

        assertAll(() -> {
            assertNotNull(cachedCard);
            assertEquals(savedCard.getNumber(), cachedCard.getNumber());
            assertEquals(savedCard.getHolder(), cachedCard.getHolder());
            assertEquals(savedCard.getExpirationDate(), cachedCard.getExpirationDate());
        });
    }

}
