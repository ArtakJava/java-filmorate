package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractCreatableStorage;

@Component
public class InMemoryFilmCreatableStorage extends AbstractCreatableStorage<Film> implements FilmStorage {
}