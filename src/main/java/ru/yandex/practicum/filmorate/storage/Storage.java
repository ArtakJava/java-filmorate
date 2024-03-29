package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DataStorage;

import java.util.List;

public interface Storage<T extends DataStorage> {
    T get(long id);

    List<T> getAll();
}