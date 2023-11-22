package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Qualifier
@Primary
@Component
@RequiredArgsConstructor
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public User create(User u) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getLogin());
            stmt.setString(3, u.getName());
            stmt.setDate(4, Date.valueOf(u.getBirthday()));
            return stmt;
        }, keyHolder);
        u.setId(keyHolder.getKey().longValue());
        if (getId(u.getId()) == null) {
            throw new NotFoundException(String.format("Добавление не произошло %s ", u));
        }
        log.info("Пользователь с идентификатором {} добавлен.", u.getId());
        return u;
    }

    @Override
    public User update(User u) {
        if (getId(u.getId()) == null) {
            throw new NotFoundException(String.format("Обновление невозможно %s не сущесвует", u));
        }
        String sqlQuery = "UPDATE USERS SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? WHERE USER_ID=?;";
        jdbcTemplate.update(sqlQuery, u.getEmail(), u.getLogin(), u.getName(), u.getBirthday(), u.getId());
        return getId(u.getId());
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, UserDBStorage::createUser);
    }

    static User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday((rs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }

    @Override
    public User getId(Long id) {
        String sqlQuery = "select * from USERS where USER_ID=?;";
        List<User> users = jdbcTemplate.query(sqlQuery, UserDBStorage::createUser, id);
        if (users.size() != 1) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь с идентификатором id не найден.");
        }
        log.info("Найден пользователь: {} {}", users.get(0).getId(), users.get(0).getLogin());
        return users.get(0);
    }

    @Override
    public boolean delete(User user) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID=?;";
        return jdbcTemplate.update(sqlQuery, user.getId()) > 0;
    }
}