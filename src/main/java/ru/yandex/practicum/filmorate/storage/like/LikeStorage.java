package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface LikeStorage extends Storage<Like> {
    void addLike(long filmId, long userId);

    boolean removeLike(long filmId, long userId);
}