package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info(InfoMessage.GET_CREATE_REQUEST, user);
        return service.create(user, bindingResult);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info(InfoMessage.GET_UPDATE_REQUEST, user);
        return service.update(user, bindingResult);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(InfoMessage.ADD_FRIEND_REQUEST, id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(InfoMessage.ADD_FRIEND_REQUEST, id, friendId);
        service.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info(InfoMessage.GET_FRIEND_LIST, id);
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info(InfoMessage.GET_COMMON_FRIEND_LIST, id, otherId);
        return service.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info(InfoMessage.GET_REQUEST, id);
        return service.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.info(InfoMessage.GET_ALL_REQUEST);
        return service.getAll();
    }
}