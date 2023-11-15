package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryMainStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends MainService<Film> {
    @Autowired
    private UserService userService;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    public FilmService(InMemoryMainStorage<Film> inMemoryMainStorage) {
        super(inMemoryMainStorage);
    }

    public Film addLike(long idFilm, long idUser) {
        userService.getId(idUser);
        Film film = getId(idFilm);
        film.getLike().add(idUser);
        return film;
    }

    public List<Film> popularList(Integer count) {
        return getAll()
                .stream()
                .sorted((o1, o2) -> o2.getLike().size() - o1.getLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }


    public Film deleteLike(long idFilm, long idUser) {
        userService.getId(idUser);
        Film film = getId(idFilm);
        film.getLike().remove(idUser);
        return film;
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException(String.format("Дата релиза раньше %s", minReleaseDate));
        }
    }
}
