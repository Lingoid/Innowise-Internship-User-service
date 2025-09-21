package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.CardDTO;
import com.innowise.userservice.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {

    CardDTO fromEntity(Card card);
    Card toEntity(CardDTO cardDTO);
}
