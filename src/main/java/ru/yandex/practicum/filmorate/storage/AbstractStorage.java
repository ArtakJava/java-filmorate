package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.DataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractStorage<T extends DataStorage> implements Storage<T> {
    private final Map<Long, T> storage = new HashMap<>();
    private long id;

    @Override
    public T create(T data) {
        data.setId(++id);
        storage.put(id, data);
        return data;
    }

    @Override
    public T update(T data) {
        T oldData = storage.get(data.getId());
        if (oldData != null) {
            storage.put(data.getId(), data);
            return data;
        } else {
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public T get(long id) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        } else {
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }
}