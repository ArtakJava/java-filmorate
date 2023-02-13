package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

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

    @GetMapping
    public List<Film> findAll() {
        return super.findAll();
    }
}