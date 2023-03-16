package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.DataStorage;
import ru.yandex.practicum.filmorate.storage.CreatableStorage;

@Slf4j
public abstract class AbstractCreatableService<T extends DataStorage> extends AbstractService<T> {
    protected CreatableStorage<T> creatableStorage;

    public AbstractCreatableService(CreatableStorage<T> creatableStorage) {
        super(creatableStorage);
        this.creatableStorage = creatableStorage;
    }

    public T create(T data, BindingResult bindingResult) {
        validate(data, bindingResult);
        log.info(InfoMessage.SUCCESS_CREATE, data);
        return creatableStorage.create(data);
    }

    public T update(T data, BindingResult bindingResult) {
        validate(data, bindingResult);
        log.info(InfoMessage.SUCCESS_UPDATE, data);
        return creatableStorage.update(data);
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