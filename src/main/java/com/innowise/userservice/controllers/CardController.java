package com.innowise.userservice.controllers;

import com.innowise.userservice.dto.CardDTO;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.Card;
import com.innowise.userservice.services.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    public CardController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO) {
        Card created = cardService.createCard(cardMapper.toEntity(cardDTO));
        return ResponseEntity.status(201).body(cardMapper.fromEntity(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCard(@PathVariable Long id) {
        Card card = cardService.getCardById(id);
        return ResponseEntity.ok(cardMapper.fromEntity(card));
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> getCards(@RequestParam List<Long> ids) {
        List<Card> cards = cardService.getCardsByIds(ids);
        return ResponseEntity.ok(cards.stream().map(cardMapper::fromEntity).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable Long id,
                                              @Valid @RequestBody CardDTO cardDTO) {
        Card card = cardMapper.toEntity(cardDTO);
        card.setId(id);

        Card updatedCard = cardService.updateCard(card);
        return ResponseEntity.ok(cardMapper.fromEntity(updatedCard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}
