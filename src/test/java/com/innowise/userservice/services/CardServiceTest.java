package com.innowise.userservice.services;

import com.innowise.userservice.dao.CardDAO;
import com.innowise.userservice.dto.CardDTO;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.Card;
import com.innowise.userservice.util.CardNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardDAO cardDAO;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private CardDTO cardDTO;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setId(1L);
        card.setNumber("123456789012");
        card.setHolder("Maksim");
        card.setExpirationDate(LocalDate.of(2030, 12, 31));

        cardDTO = new CardDTO();
        cardDTO.setId(1L);
        cardDTO.setNumber("123456789012");
        cardDTO.setHolder("Maksim");
        cardDTO.setExpirationDate(LocalDate.of(2030, 12, 31));
    }

    @Test
    void createCard_returnsCardDTO() {
        when(cardMapper.toEntity(cardDTO)).thenReturn(card);
        when(cardMapper.fromEntity(card)).thenReturn(cardDTO);

        CardDTO result = cardService.createCard(cardDTO);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals("Maksim", result.getHolder());
            assertEquals("123456789012", result.getNumber());
        });

        verify(cardMapper).toEntity(cardDTO);
        verify(cardDAO).createCard(card);
        verify(cardMapper).fromEntity(card);

    }

    @Test
    void getCardById_returnsCardDTO_whenCardExists() {
        when(cardDAO.getCardById(1L)).thenReturn(card);
        when(cardMapper.fromEntity(card)).thenReturn(cardDTO);

        CardDTO result = cardService.getCardById(1L);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals("Maksim", result.getHolder());
        });

        verify(cardDAO).getCardById(1L);
        verify(cardMapper).fromEntity(card);
    }

    @Test
    void getCardById_throwsException_whenCardNotFound() {
        when(cardDAO.getCardById(1L)).thenReturn(null);

        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(1L));

        verify(cardDAO).getCardById(1L);
    }

    @Test
    void getCardsByIds_returnsListOfCardDTOs() {
        when(cardDAO.getCardsByIds(Mockito.anyList())).thenReturn(List.of(card));
        when(cardMapper.fromEntity(card)).thenReturn(cardDTO);

        List<CardDTO> result = cardService.getCardsByIds(List.of(1L));

        assertAll(() -> {
            assertEquals(1, result.size());
            assertEquals("Maksim", result.get(0).getHolder());
        });

        verify(cardDAO).getCardsByIds(List.of(1L));
        verify(cardMapper).fromEntity(card);
    }

    @Test
    void updateCard_returnsUpdatedCardDTO_whenCardExists() {
        when(cardMapper.toEntity(cardDTO)).thenReturn(card);
        when(cardDAO.getCardById(1L)).thenReturn(card);
        when(cardMapper.fromEntity(card)).thenReturn(cardDTO);

        CardDTO result = cardService.updateCard(1L, cardDTO);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals("Maksim", result.getHolder());
        });

        verify(cardMapper).toEntity(cardDTO);
        verify(cardDAO).getCardById(1L);
        verify(cardDAO).updateCardById(card);
        verify(cardMapper).fromEntity(card);
    }

    @Test
    void updateCard_throwsException_whenCardNotFound() {
        when(cardMapper.toEntity(cardDTO)).thenReturn(card);
        when(cardDAO.getCardById(1L)).thenReturn(null);

        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(1L, cardDTO));

        verify(cardMapper).toEntity(cardDTO);
        verify(cardDAO).getCardById(1L);
    }

    @Test
    void deleteCard_completes_whenCardExists() {
        when(cardDAO.getCardById(1L)).thenReturn(card);

        assertDoesNotThrow(() -> cardService.deleteCard(1L));

        verify(cardDAO).getCardById(1L);
        verify(cardDAO).deleteCardById(1L);
    }

    @Test
    void deleteCard_throwsException_whenCardNotFound() {
        when(cardDAO.getCardById(1L)).thenReturn(null);

        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(1L));

        verify(cardDAO).getCardById(1L);
    }
}