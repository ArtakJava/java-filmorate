package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.AbstractService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    public UserController(AbstractService<User> service) {
        super(service);
    }

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

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(InfoMessage.ADD_FRIEND_REQUEST + id + " и " + friendId);
        service.addFriend(id, friendId);
        log.info(InfoMessage.SUCCESS_ADD_FRIEND + id + " и " + friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(InfoMessage.ADD_FRIEND_REQUEST + id + " и " + friendId);
        service.removeFriend(id, friendId);
        log.info(InfoMessage.SUCCESS_ADD_FRIEND + id + " и " + friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info(InfoMessage.GET_FRIEND_LIST + id);
        List<User> friends = service.getFriends(id);
        log.info(InfoMessage.SUCCESS_FRIEND_LIST + id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info(InfoMessage.GET_COMMON_FRIEND_LIST + id + " и " + otherId);
        List<User> commonFriends = service.getCommonFriends(id, otherId);
        log.info(InfoMessage.SUCCESS_COMMON_FRIEND_LIST + id + " и " + otherId);
        return commonFriends;
    }

    @DeleteMapping({"/{id}", ""})
    public void delete(@PathVariable(required = false) Long id) {
        if (id != null) {
            log.info(InfoMessage.DELETE_REQUEST + id);
            super.delete(id);
            log.info(InfoMessage.SUCCESS_DELETE + id);
        } else {
            log.info(InfoMessage.DATA_NOT_EXIST);
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        if (id != null) {
            log.info(InfoMessage.GET_REQUEST + id);
            User user = super.get(id);
            log.info(InfoMessage.SUCCESS_GET + id);
            return user;
        } else {
            log.info(InfoMessage.DATA_NOT_EXIST);
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
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