package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.AbstractService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    public FilmController(AbstractService<Film> service) {
        super(service);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.info(InfoMessage.GET_CREATE_REQUEST + film);
        Film result = super.create(film, bindingResult);
        log.info(InfoMessage.SUCCESS_CREATE + result);
        return result;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.info(InfoMessage.GET_UPDATE_REQUEST + film);
        Film result = super.update(film, bindingResult);
        log.info(InfoMessage.SUCCESS_UPDATE + film);
        return result;
    }

    @DeleteMapping({"/{id}", ""})
    public void delete(@PathVariable(required = false) Long id) {
        if (id != null) {
            log.info(InfoMessage.DELETE_REQUEST + id);
            super.delete(id);
            log.info(InfoMessage.SUCCESS_DELETE + id);
        } else {
            log.info(InfoMessage.DATA_NOT_EXIST);
        }
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info(InfoMessage.GET_ADD_LIKE_REQUEST + userId + " и " + id);
        service.addLike(id, userId);
        log.info(InfoMessage.SUCCESS_ADD_LIKE + userId + id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info(InfoMessage.GET_REMOVE_LIKE_REQUEST + userId + " и "  + id);
        service.removeLike(id, userId);
        log.info(InfoMessage.SUCCESS_REMOVE_LIKE + userId + " и "  + id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") String count) {
        log.info(InfoMessage.GET_POPULAR_FILMS + count);
        List<Film> films = service.getPopular(Integer.parseInt(count));
        log.info(InfoMessage.SUCCESS_POPULAR_FILMS + count);
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable(required = false) Long id) {
        if (id != null) {
            log.info(InfoMessage.GET_REQUEST + id);
            Film film = super.get(id);
            log.info(InfoMessage.SUCCESS_GET + id);
            return film;
        } else {
            log.info(InfoMessage.DATA_NOT_EXIST);
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
    }

    @GetMapping
    public List<Film> findAll() {
        return super.findAll();
    }
}