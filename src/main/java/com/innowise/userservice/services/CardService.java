package com.innowise.userservice.services;

import com.innowise.userservice.dao.CardDAO;
import com.innowise.userservice.model.Card;
import com.innowise.userservice.util.CardNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardDAO cardDAO;

    public CardService(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    public Card createCard(Card card) {
        cardDAO.createCard(card);
        return card;
    }

    public Card getCardById(Long id) {
        return Optional.ofNullable(cardDAO.getCardById(id))
                .orElseThrow(CardNotFoundException::new);
    }

    public List<Card> getCardsByIds(List<Long> ids) {
        return cardDAO.getCardsByIds(ids);
    }

    @Transactional
    public Card updateCard(Card card) {
        Optional.ofNullable(cardDAO.getCardById(card.getId()))
                .orElseThrow(CardNotFoundException::new);
        cardDAO.updateCardById(card);
        return card;
    }

    @Transactional
    public void deleteCard(Long id) {
        Optional.ofNullable(cardDAO.getCardById(id))
                .orElseThrow(CardNotFoundException::new);
        cardDAO.deleteCardById(id);
    }
}
