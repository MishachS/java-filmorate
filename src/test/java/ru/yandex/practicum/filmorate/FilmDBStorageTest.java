package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.database.FilmDBStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDBStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = Genre.builder().id(1L).name("Комедия").build();
        genres.add(genre);
        Film newFilm = Film.builder()
                .id(1L)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2002, 11, 12))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        FilmDBStorage filmStorage = new FilmDBStorage(jdbcTemplate);
        filmStorage.create(newFilm);

        Film savedFilm = filmStorage.getId(newFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testFindFilmUpdate() {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = Genre.builder().id(1L).name("Комедия").build();
        genres.add(genre);
        Film newFilm = Film.builder()
                .id(1L)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2002, 11, 12))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        FilmDBStorage filmStorage = new FilmDBStorage(jdbcTemplate);
        filmStorage.create(newFilm);
        Film updateFilm = Film.builder()
                .id(1L)
                .name("testFilmNew")
                .description("testFilmNew")
                .releaseDate(LocalDate.of(2023, 11, 20))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        Film savedFilm = filmStorage.update(updateFilm);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updateFilm);
    }

    @Test
    public void testFindFilmDelete() {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = Genre.builder().id(1L).name("Комедия").build();
        genres.add(genre);
        Film newFilm = Film.builder()
                .id(1L)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2002, 11, 12))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        FilmDBStorage filmStorage = new FilmDBStorage(jdbcTemplate);
        filmStorage.create(newFilm);
        Film updateFilm = Film.builder()
                .id(1L)
                .name("testFilmNew")
                .description("testFilmNew")
                .releaseDate(LocalDate.of(2023, 11, 20))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();
        filmStorage.create(updateFilm);

        filmStorage.delete(newFilm);
        List<Film> list = filmStorage.getAll();

        assertThat(list.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updateFilm);
        assertThat(list.size())
                .isEqualTo(1);
    }

}