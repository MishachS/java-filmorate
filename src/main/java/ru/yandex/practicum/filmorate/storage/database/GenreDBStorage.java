package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, GenreDBStorage::createGenre);
    }

    @Override
    public Genre getId(Long id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDBStorage::createGenre, id);
        if (genres.size() != 1) {
            throw new NotFoundException("Genre with id is not single");
        }
        return genres.get(0);
    }

    static Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("GENRE_ID"))
                .name(rs.getString("NAME"))
                .build();
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

    @Override
    public Genre create(Genre data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Genre update(Genre data) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean delete(Genre data) {
        throw new UnsupportedOperationException();
    }

}