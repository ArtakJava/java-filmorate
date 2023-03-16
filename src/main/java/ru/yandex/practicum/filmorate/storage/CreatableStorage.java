package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DataStorage;

public interface CreatableStorage<T extends DataStorage> extends Storage<T> {
    T create(T data);

    T update(T data);
}