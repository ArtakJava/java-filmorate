package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@Service
public class FilmService extends AbstractCreatableService<Film> {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    public FilmService(@Qualifier("FilmDbStorage") FilmDbStorage filmDbStorage,
                       @Qualifier("UserDbStorage") UserDbStorage userDbStorage) {
        super(filmDbStorage);
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public void addLike(long filmId, long userId) {
        userDbStorage.get(userId);
        filmDbStorage.addLike(filmId, userId);
        log.info(InfoMessage.SUCCESS_ADD_LIKE, userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        userDbStorage.get(userId);
        filmDbStorage.removeLike(filmId, userId);
        log.info(InfoMessage.SUCCESS_REMOVE_LIKE, userId, filmId);
    }

    public List<Film> getPopular(int count) {
        List<Film> popularFilms = filmDbStorage.getPopular(count);
        log.info(InfoMessage.SUCCESS_POPULAR_FILMS + count);
        return popularFilms;
    }
}