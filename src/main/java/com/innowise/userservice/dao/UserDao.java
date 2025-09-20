package com.innowise.userservice.dao;

import com.innowise.userservice.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;


@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SQL {
        private static final String INSERT_USER =
                "INSERT INTO users (name, surname, birth_date, email) " +
                        "VALUES (:name, :surname, :birth_date, :email)";

        private static final String SELECT_BY_ID =
                "SELECT * FROM users WHERE id = :id";

        private static final String SELECT_BY_IDS =
                "SELECT * FROM users WHERE id IN (:ids)";

        private static final String SELECT_BY_EMAIL =
                "SELECT * FROM users WHERE email = :email";

        private static final String UPDATE_USER =
                "UPDATE users SET name = :name, surname = :surname, " +
                        "birth_date = :birth_date, email = :email WHERE id = :id";

        private static final String DELETE_BY_ID =
                "DELETE FROM users WHERE id = :id";
    }


    public int createUser(User user) {
        return jdbcTemplate.update(SQL.INSERT_USER, new BeanPropertySqlParameterSource(user));
    }

    public User getUserById(Long id) {
        return jdbcTemplate.queryForObject(SQL.SELECT_BY_ID,
                Collections.singletonMap("id", id),
                new BeanPropertyRowMapper<>(User.class));
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return jdbcTemplate.query(SQL.SELECT_BY_IDS,
                Collections.singletonMap("ids", ids),
                new BeanPropertyRowMapper<>(User.class));
    }

    public User getUserByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL.SELECT_BY_EMAIL,
                Collections.singletonMap("email", email),
                new BeanPropertyRowMapper<>(User.class));
    }

    public int updateUserById(User user) {
        return jdbcTemplate.update(SQL.UPDATE_USER, new BeanPropertySqlParameterSource(user));
    }

    public int deleteUserById(Long id) {
        return jdbcTemplate.update(SQL.DELETE_BY_ID, Collections.singletonMap("id", id));
    }



}
