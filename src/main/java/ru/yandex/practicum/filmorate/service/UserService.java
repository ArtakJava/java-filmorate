package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService<User> {
    public UserService(Storage<User> storage) {
        super(storage);
    }

    public void addFriend(long userId, long friendId) {
        if (userId > 0 && friendId > 0) {
            storage.get(userId).getFriendsId().add(friendId);
            storage.get(friendId).getFriendsId().add(userId);
            log.info(InfoMessage.SUCCESS_ADD_FRIEND, userId, friendId);
        } else {
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
    }

    public void removeFriend(long userId, long friendId) {
        if (userId > 0 && friendId > 0) {
            storage.get(userId).getFriendsId().remove(friendId);
            storage.get(friendId).getFriendsId().remove(userId);
            log.info(InfoMessage.SUCCESS_ADD_FRIEND, userId, friendId);
        } else {
            throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
        }
    }

    public List<User> getFriends(long userId) {
        List<User> friends = storage.get(userId).getFriendsId().stream()
                .map(this::get)
                .collect(Collectors.toList());
        log.info(InfoMessage.SUCCESS_FRIEND_LIST + userId);
        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        List<User> commonFriends = storage.get(userId).getFriendsId().stream()
                .filter(friend -> storage.get(otherUserId).getFriendsId().contains(friend))
                .map(this::get)
                .collect(Collectors.toList());
        log.info(InfoMessage.SUCCESS_COMMON_FRIEND_LIST, userId, otherUserId);
        return commonFriends;
    }
}