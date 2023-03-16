package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Service
public class UserService extends AbstractCreatableService<User> {
    protected final UserDbStorage userDbStorage;

    public UserService(@Qualifier("UserDbStorage") UserDbStorage userDbStorage) {
        super(userDbStorage);
        this.userDbStorage = userDbStorage;
    }

    public void addFriend(long userId, long friendId) {
        userDbStorage.addFriend(userId, friendId);
        log.info(InfoMessage.SUCCESS_ADD_FRIEND, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userDbStorage.removeFriend(userId, friendId);
        log.info(InfoMessage.SUCCESS_DELETE_FRIEND, userId, friendId);
    }

    public List<User> getFriends(long userId) {
        List<User> friends = userDbStorage.getFriends(userId);
        log.info(InfoMessage.SUCCESS_FRIEND_LIST, userId);
        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        List<User> commonFriends = userDbStorage.getCommonFriends(userId, otherUserId);
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