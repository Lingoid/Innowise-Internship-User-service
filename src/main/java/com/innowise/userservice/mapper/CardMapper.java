package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.CardDTO;
import com.innowise.userservice.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {

    @Mapping(source = "expirationDate", target = "expirationDate", dateFormat = "yyyy-MM-dd")
    CardDTO fromEntity(Card card);

    @Mapping(source = "expirationDate", target = "expirationDate", dateFormat = "yyyy-MM-dd")
    Card toEntity(CardDTO cardDTO);
}
