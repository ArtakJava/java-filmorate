package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Like extends DataStorage {
    public long userId;

    public Like(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Like like = (Like) o;
        return userId == like.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
}