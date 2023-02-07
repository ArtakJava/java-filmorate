package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();
    private static int filmId;

    @PostMapping()
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        String message;
        if (bindingResult.hasErrors()) {
            StringBuilder bd = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
               bd.append(error.getDefaultMessage());
            }
            message = bd.toString();
            log.error(message);
            throw new ValidationException(message);
        } else {
            film.setId(++filmId);
            films.put(filmId, film);
            return film;
        }
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film, BindingResult bindingResult) {
        String message;
        if (films.containsKey(film.getId())) {
            if (bindingResult.hasErrors()) {
                StringBuilder bd = new StringBuilder();
                for (ObjectError error : bindingResult.getAllErrors()) {
                    bd.append(error.getDefaultMessage());
                }
                message = bd.toString();
                log.error(message);
                throw new ValidationException(message);
            } else {
                films.put(filmId, film);
                return film;
            }
        } else {
            throw new ValidationException("Такого фильма нет.");
        }
    }

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }
}