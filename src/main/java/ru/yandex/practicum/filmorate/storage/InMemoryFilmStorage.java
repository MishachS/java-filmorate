package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Deprecated
public class InMemoryFilmStorage extends InMemoryMainStorage<Film> implements FilmStorage {
}
