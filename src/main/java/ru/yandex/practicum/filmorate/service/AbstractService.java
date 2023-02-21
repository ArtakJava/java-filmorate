package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.DataStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractService<T extends DataStorage> {
    final Storage<T> storage;

    public T create(T data) {
        return storage.create(data);
    }

    public T update(T data) {
        return storage.update(data);
    }

    public void delete(long id) {
        storage.delete(id);
    }

    public T get(long id) {
        return storage.get(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }

    public void addFriend(long userId, long friendId){}

    public void removeFriend(long userId, long friendId){}

    public List<User> getFriends(long userId){
        return null;
    }

    public List<User> getCommonFriends(long id, long otherId){
        return null;
    }

    public void addLike(long filmId, long userId) {}

    public void removeLike(long filmId, long userId) {}

    public List<Film> getPopular(int count) {
        return null;
    }
}