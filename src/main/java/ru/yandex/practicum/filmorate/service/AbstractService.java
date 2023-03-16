package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.DataStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Slf4j
public abstract class AbstractService<T extends DataStorage> {
    protected Storage<T> storage;


    public AbstractService(Storage<T> storage) {
        this.storage = storage;
    }

    public T get(long id) {
        T data = storage.get(id);
        log.info(InfoMessage.SUCCESS_GET, id);
        return data;
    }

    public List<T> getAll() {
        List<T> allData = storage.getAll();
        log.info(InfoMessage.SUCCESS_GET_ALL);
        return allData;
    }
}