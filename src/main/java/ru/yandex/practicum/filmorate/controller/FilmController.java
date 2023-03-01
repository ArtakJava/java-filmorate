package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.info(InfoMessage.GET_CREATE_REQUEST + film);
        return service.create(film, bindingResult);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.info(InfoMessage.GET_UPDATE_REQUEST + film);
        return service.update(film, bindingResult);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info(InfoMessage.GET_ADD_LIKE_REQUEST, userId, id);
        service.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info(InfoMessage.GET_REMOVE_LIKE_REQUEST, userId, id);
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") String count) {
        log.info(InfoMessage.GET_POPULAR_FILMS + count);
        return service.getPopular(Integer.parseInt(count));
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable(required = false) Long id) {
        log.info(InfoMessage.GET_REQUEST + id);
        return service.get(id);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info(InfoMessage.GET_ALL_REQUEST);
        return service.getAll();
    }
}