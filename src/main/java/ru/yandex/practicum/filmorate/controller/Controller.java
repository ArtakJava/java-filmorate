package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.messageManager.ErrorMessage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class Controller<T extends Item> {
    private final Map<Integer, T> storage = new HashMap<>();
    private int id;

    public T create(T body, BindingResult bindingResult) {
        if (isValid(bindingResult)) {
            body.setId(++id);
            storage.put(id, body);
        }
        return body;
    }

    public T update(T body, BindingResult bindingResult) {
        if (storage.containsKey(body.getId())) {
            if (isValid(bindingResult)) {
                storage.put(id, body);
            }
            return body;
        } else {
            throw new ValidationException(ErrorMessage.ITEM_NOT_EXIST);
        }
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    private boolean isValid(BindingResult bindingResult) {
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
            return true;
        }
    }
}