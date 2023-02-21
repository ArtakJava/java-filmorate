package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {
    private final Storage<User> userStorage;

    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        super(storage);
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(long filmId, long userId) {
        userStorage.get(userId);
        storage.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        userStorage.get(userId);
        storage.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(Comparator.comparingInt(o -> - o.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}