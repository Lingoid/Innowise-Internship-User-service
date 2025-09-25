package com.innowise.userservice.services;

import com.innowise.userservice.dao.CardDAO;
import com.innowise.userservice.dto.CardDTO;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.Card;
import com.innowise.userservice.util.CardNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardDAO cardDAO;
    private final CardMapper cardMapper;

    public CardService(CardDAO cardDAO, CardMapper cardMapper) {
        this.cardDAO = cardDAO;
        this.cardMapper = cardMapper;
    }

    public CardDTO createCard(CardDTO cardDTO) {
        Card card = cardMapper.toEntity(cardDTO);
        cardDAO.createCard(card);
        return cardMapper.fromEntity(card);
    }

    public CardDTO getCardById(Long id) {
        Card card = Optional.ofNullable(cardDAO.getCardById(id))
                .orElseThrow(CardNotFoundException::new);
        return cardMapper.fromEntity(card);
    }

    public List<CardDTO> getCardsByIds(List<Long> ids) {
        return cardDAO.getCardsByIds(ids).stream()
                .map(cardMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardDTO updateCard(Long id, CardDTO cardDTO) {
        Card card = cardMapper.toEntity(cardDTO);
        card.setId(id);

        Optional.ofNullable(cardDAO.getCardById(card.getId()))
                .orElseThrow(CardNotFoundException::new);
        cardDAO.updateCardById(card);
        return cardMapper.fromEntity(card);
    }

    @Transactional
    public void deleteCard(Long id) {
        Optional.ofNullable(cardDAO.getCardById(id))
                .orElseThrow(CardNotFoundException::new);
        cardDAO.deleteCardById(id);
    }
}
