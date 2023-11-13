package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends MainController<Film>{

    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getListFilms(){
        log.info("Get films");
        return getMap();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        log.info("Add film{}", film);
        return create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        log.info("Update film{}", film);
        return update(film);
    }

    @Override
    public void validate(Film film){
        if(film.getReleaseDate().isBefore(minReleaseDate)){
            throw new NotFoundException(String.format("Дата релиза раньше %s", minReleaseDate));
        }
    }
}
