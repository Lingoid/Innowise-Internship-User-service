package com.innowise.userservice.dao;

import com.innowise.userservice.model.Card;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;

@Repository
public class CardDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CardDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SQL {
        private static final String INSERT_CARD =
                "INSERT INTO card_info (user_id, number, holder, expiration_date) " +
                        "VALUES (:userId, :number, :holder, :expiration_date)";

        private static final String SELECT_BY_ID =
                "SELECT * FROM card_info WHERE id = :id";

        private static final String SELECT_BY_IDS =
                "SELECT * FROM card_info WHERE id IN (:ids)";

        private static final String UPDATE_CARD =
                "UPDATE card_info SET user_id = :userId, number = :number, " +
                        "holder = :holder, expiration_date = :expiration_date WHERE id = :id";

        private static final String DELETE_BY_ID =
                "DELETE FROM card_info WHERE id = :id";
    }

    public int createCard(Card card) {
        return jdbcTemplate.update(SQL.INSERT_CARD, new BeanPropertySqlParameterSource(card));
    }

    public Card getCardById(Long id) {
        return jdbcTemplate.queryForObject(SQL.SELECT_BY_ID,
                Collections.singletonMap("id", id),
                new BeanPropertyRowMapper<>(Card.class));
    }

    public List<Card> getCardsByIds(List<Long> ids) {
        return jdbcTemplate.query(SQL.SELECT_BY_IDS,
                Collections.singletonMap("ids", ids),
                new BeanPropertyRowMapper<>(Card.class));
    }

    public int updateCardById(Card card) {
        return jdbcTemplate.update(SQL.UPDATE_CARD, new BeanPropertySqlParameterSource(card));
    }

    public int deleteCardById(Long id) {
        return jdbcTemplate.update(SQL.DELETE_BY_ID,
                Collections.singletonMap("id", id));
    }
}
