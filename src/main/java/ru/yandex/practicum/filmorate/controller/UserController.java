package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @Override
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        setName(user);
        return super.create(user, bindingResult);
    }

    @Override
    public User update(@Valid @RequestBody User user, BindingResult bindingResult) {
        setName(user);
        return super.update(user, bindingResult);
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}