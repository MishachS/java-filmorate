package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController extends MainController<Film> {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getListFilms() {
        log.info("Get films");
        return filmService.getAll();
    }

    @GetMapping("/popular")
    public List<Film> popularFilm(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Popular film");
        return filmService.popularList(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film{}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film{}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Like film{}", id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete like film{}", id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("{id}")
    public Film getForId(@PathVariable Long id) {
        log.info("Get film id{}", id);
        return filmService.getId(id);
    }

}
