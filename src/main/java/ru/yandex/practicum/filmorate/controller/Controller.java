package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.AbstractService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class Controller<T extends DataStorage> {
    final AbstractService<T> service;

    public T create(T data, BindingResult bindingResult) {
        if (isValid(bindingResult)) {
            service.create(data);
        }
        return data;
    }

    public T update(T data, BindingResult bindingResult) {
        if (isValid(bindingResult)) {
            service.update(data);
        }
        return data;
    }

    public void delete(long id) {
        service.delete(id);
    }

    public T get(long id) {
        return service.get(id);
    }

    public List<T> findAll() {
        return service.getAll();
    }

    private boolean isValid(BindingResult bindingResult) {
        String message;
        if (bindingResult.hasErrors()) {
            StringBuilder bd = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                bd.append("\n").append(error.getDefaultMessage());
            }
            message = bd.toString();
            log.error(message);
            throw new ValidationException(message);
        } else {
            return true;
        }
    }
}