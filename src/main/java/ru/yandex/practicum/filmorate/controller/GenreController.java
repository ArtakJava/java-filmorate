package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Long id) {
        log.info(InfoMessage.GET_REQUEST, id);
        return service.get(id);
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info(InfoMessage.GET_ALL_REQUEST);
        return service.getAll();
    }
}