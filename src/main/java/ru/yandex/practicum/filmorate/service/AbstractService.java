package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.DataStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<T extends DataStorage> {
    protected final Storage<T> storage;

    public T create(T data, BindingResult bindingResult) {
        validate(data, bindingResult);
        log.info(InfoMessage.SUCCESS_CREATE + data);
        return storage.create(data);
    }

    public T update(T data, BindingResult bindingResult) {
        validate(data, bindingResult);
        log.info(InfoMessage.SUCCESS_UPDATE + data);
        return storage.update(data);
    }

    public T get(long id) {
        T data = storage.get(id);
        log.info(InfoMessage.SUCCESS_GET + id);
        return data;
    }

    public List<T> getAll() {
        List<T> allData = storage.getAll();
        log.info(InfoMessage.SUCCESS_GET_ALL);
        return allData;
    }

    protected void validate(T data, BindingResult bindingResult) {
        String message;
        if (bindingResult.hasErrors()) {
            StringBuilder bd = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                bd.append(System.lineSeparator()).append(error.getDefaultMessage());
            }
            message = bd.toString();
            log.error(message);
            throw new ValidationException(message);
        }
    }
}