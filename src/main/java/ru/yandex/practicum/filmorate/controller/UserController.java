package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();
    private static int userId;

    @PostMapping()
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
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
            if (user.getLogin().contains(" ")) {
                message = "Логин не может содержать пробелы.";
                log.error(message);
                throw new ValidationException(message);
            } else {
                if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                user.setId(++userId);
                users.put(userId, user);
                return user;
            }
        }
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user, BindingResult bindingResult) {
        String message;
        if (users.containsKey(user.getId())) {
            if (bindingResult.hasErrors()) {
                StringBuilder bd = new StringBuilder();
                for (ObjectError error : bindingResult.getAllErrors()) {
                    bd.append(error.getDefaultMessage());
                }
                message = bd.toString();
                log.error(message);
                throw new ValidationException(message);
            } else {
                if (user.getLogin().contains(" ")) {
                    message = "Логин не может содержать пробелы.";
                    log.error(message);
                    throw new ValidationException(message);
                } else {
                    if (user.getName().isBlank()) {
                        user.setName(user.getLogin());
                    }
                    users.put(user.getId(), user);
                    return user;
                }
            }
        } else {
            throw new ValidationException("Такого пользователя нет.");
        }
    }

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}