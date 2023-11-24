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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Qualifier
@Primary
@Component
@RequiredArgsConstructor
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES(?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String genreSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES(?, ?)";
                jdbcTemplate.update(genreSql, film.getId(), genre.getId());
            }
        }
        return getId(film.getId());
    }


    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILM SET FILM_ID=?, NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, RATING_ID=? " +
                "WHERE FILM_ID=?;";
        jdbcTemplate.update(sqlQuery, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        String sqlDelete = "DELETE FROM FILM_GENRE WHERE FILM_ID=?;";
        jdbcTemplate.update(sqlDelete, film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String genreSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES(?, ?)";
                jdbcTemplate.update(genreSql, film.getId(), genre.getId());
            }
        }
        return getId(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID, R.RATING_TITLE " +
                "FROM FILM AS F INNER JOIN RATING AS R ON F.RATING_ID = R.RATING_ID; ";
        List<Film> list = jdbcTemplate.query(sqlQuery, FilmDBStorage::createFilm);
        for (int i = 0; i < list.size(); i++) {
            genresForFilm(list.get(i));
        }
        return list;
    }

    @Override
    public Film getId(Long id) {
        String sqlQuery = "select F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID, R.RATING_TITLE" +
                " from FILM AS F LEFT JOIN RATING AS R ON F.RATING_ID=R.RATING_ID" +
                " where F.FILM_ID = ?;";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDBStorage::createFilm, id);
        if (films.size() != 1) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь с идентификатором id не найден.");
        }
        log.info("Найден фильм: {} {}", id, films.get(0).getName());
        genresForFilm(films.get(0));
        return films.get(0);


    }

    public void genresForFilm(Film film) {
        String sqlQueryGenre = "select F.FILM_ID, G1.NAME, G2.GENRE_ID " +
                "from FILM AS F " +
                "INNER JOIN FILM_GENRE AS G2 ON F.FILM_ID = G2.FILM_ID " +
                "LEFT OUTER JOIN GENRE AS G1 ON G2.GENRE_ID=G1.GENRE_ID " +
                "where F.FILM_ID = ?;";
        LinkedHashSet<Genre> set = new LinkedHashSet<>(jdbcTemplate.query(sqlQueryGenre, GenreDBStorage::createGenre, film.getId()));
        if (!set.isEmpty()) {
            film.setGenres(set);
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
    }

    static Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate((rs.getDate("RELEASE_DATE")).toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(Mpa.builder()
                        .id(rs.getLong("RATING_ID"))
                        .name(rs.getString("RATING_TITLE"))
                        .build())
                .build();
    }

    @Override
    public boolean delete(Film data) {
        String sqlQuery = "DELETE FROM FILM WHERE FILM_ID=?;";
        return jdbcTemplate.update(sqlQuery, data.getId()) > 0;
    }
}
