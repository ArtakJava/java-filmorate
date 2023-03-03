package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService<User> {
    public UserService(Storage<User> storage) {
        super(storage);
    }

    public void addFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        storage.get(userId).getFriendsId().add(friend.getId());
        storage.get(friendId).getFriendsId().add(user.getId());
        log.info(InfoMessage.SUCCESS_ADD_FRIEND, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        storage.get(userId).getFriendsId().remove(friend.getId());
        storage.get(friendId).getFriendsId().remove(user.getId());
        log.info(InfoMessage.SUCCESS_ADD_FRIEND, userId, friendId);
    }

    public List<User> getFriends(long userId) {
        List<User> friends = storage.get(userId).getFriendsId().stream()
                .map(this::get)
                .collect(Collectors.toList());
        log.info(InfoMessage.SUCCESS_FRIEND_LIST, userId);
        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        Set<Long> friendsId = storage.get(otherUserId).getFriendsId();
        List<User> commonFriends = storage.get(userId).getFriendsId().stream()
                .filter(friendsId::contains)
                .map(this::get)
                .collect(Collectors.toList());
        log.info(InfoMessage.SUCCESS_COMMON_FRIEND_LIST, userId, otherUserId);
        return commonFriends;
    }

    @Override
    protected void validate(User user, BindingResult bindingResult) {
        super.validate(user, bindingResult);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}