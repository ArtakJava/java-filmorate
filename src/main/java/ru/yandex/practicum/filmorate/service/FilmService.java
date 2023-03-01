package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {
    private final Storage<User> userStorage;
    private static final Comparator<Film> comparator = Comparator.comparingLong(Film::getLikes).reversed();

    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        super(storage);
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        userStorage.get(userId);
        storage.get(filmId).getLikesStorage().add(userId);
        log.info(InfoMessage.SUCCESS_ADD_LIKE, userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        userStorage.get(userId);
        storage.get(filmId).getLikesStorage().remove(userId);
        log.info(InfoMessage.SUCCESS_REMOVE_LIKE, userId, filmId);
    }

    public List<Film> getPopular(int count) {
        List<Film> popularFilms = storage.getAll().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
        log.info(InfoMessage.SUCCESS_POPULAR_FILMS + count);
        return popularFilms;
    }
}