package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info(InfoMessage.GET_CREATE_REQUEST + user);
        setName(user);
        User result = super.create(user, bindingResult);
        log.info(InfoMessage.SUCCESS_CREATE + result);
        return result;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info(InfoMessage.GET_UPDATE_REQUEST + user);
        setName(user);
        User result = super.update(user, bindingResult);
        log.info(InfoMessage.SUCCESS_UPDATE + result);
        return result;
    }

    @GetMapping
    public List<User> findAll() {
        return super.findAll();
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}