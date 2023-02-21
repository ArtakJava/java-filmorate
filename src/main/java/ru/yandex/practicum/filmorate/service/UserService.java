package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractService<User> {
    public UserService(Storage<User> storage) {
        super(storage);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        storage.get(userId).getFriendsId().add(friendId);
        storage.get(friendId).getFriendsId().add(userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        storage.get(userId).getFriendsId().remove(friendId);
        storage.get(friendId).getFriendsId().remove(userId);
    }

    @Override
    public List<User> getFriends(long userId) {
        List<User> friends = new ArrayList<>();
        for (Long id : storage.get(userId).getFriendsId()) {
            friends.add(storage.get(id));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        List<User> userFriends = new ArrayList<>();
        List<User> otherUserFriends = new ArrayList<>();
        for (Long id : storage.get(userId).getFriendsId()) {
            userFriends.add(storage.get(id));
        }
        for (Long id : storage.get(otherUserId).getFriendsId()) {
            otherUserFriends.add(storage.get(id));
        }
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }
}