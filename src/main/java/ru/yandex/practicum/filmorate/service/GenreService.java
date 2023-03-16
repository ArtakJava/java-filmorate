package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

@Slf4j
@Service
public class GenreService extends AbstractService<Genre> {
    public GenreService(@Qualifier("GenreDbStorage") GenreDbStorage genreDbStorage) {
        super(genreDbStorage);
    }
}