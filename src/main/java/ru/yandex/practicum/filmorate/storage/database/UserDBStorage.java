package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Qualifier("userDBStorage")
@Primary
@Component
@RequiredArgsConstructor
@Slf4j
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private long generatedID = 1;

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?);";
        user.setId(generatedID++);
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        if (getId(user.getId()) == null) {
            throw new NotFoundException(String.format("Добавление не произошло %s ", user));
        }
        log.info("Пользователь с идентификатором {} добавлен.", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (getId(user.getId()) == null) {
            throw new NotFoundException(String.format("Обновление невозможно %s не сущесвует", user));
        }
        String sqlQuery = "UPDATE USERS SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? WHERE USER_ID=?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getId(user.getId());
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
            throw new NotFoundException("Пользователь с идентификатором - " + id + " не найден");
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