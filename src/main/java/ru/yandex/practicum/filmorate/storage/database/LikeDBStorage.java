package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class LikeDBStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenreDBStorage genreDBStorage;

    public void addLike(long idFilm, long idUser) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USERS_ID) VALUES(?, ?);";
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }

    public void likeDelete(long idFilm, long idUser) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID=? AND USERS_ID=?;";
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }

    public List<Film> popularListFilm(Integer count) {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, " +
                "F.RATING_ID, R.RATING_TITLE, COUNT(L.FILM_ID) " +
                "FROM FILM AS F " +
                "LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID " +
                "INNER JOIN RATING AS R ON F.RATING_ID = R.RATING_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(L.FILM_ID) DESC " +
                "LIMIT ?;";


        List<Film> list = jdbcTemplate.query(sqlQuery, FilmDBStorage::createFilm, count);
        for (int i = 0; i < list.size(); i++) {
            genreDBStorage.genresForFilm(list.get(i));
        }
        return list;
    }
}
