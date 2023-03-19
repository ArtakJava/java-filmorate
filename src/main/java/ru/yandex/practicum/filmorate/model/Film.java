package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.messageManager.ErrorMessage;
import ru.yandex.practicum.filmorate.validator.ReleaseDate.ReleaseValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Set;

@Data
public class Film extends DataStorage {
    @NotBlank(message = ErrorMessage.FILM_EMPTY_NAME)
    private String name;
    @Size(max=200, message = ErrorMessage.FILM_MAX_DESCRIPTION)
    private String description;
    @ReleaseValidator(message = ErrorMessage.FILM_RELEASE_DATE)
    private Date releaseDate;
    @Positive(message = ErrorMessage.FILM_DURATION)
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Like> likes;

    public Film(long id,
                String name,
                String description,
                Date releaseDate,
                int duration,
                Mpa mpa,
                Set<Genre> genres,
                Set<Like> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes;
    }

    public Film(String name,
                String description,
                Date releaseDate,
                int duration,
                Mpa mpa,
                Set<Genre> genres,
                Set<Like> likes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes;
    }

    public Film() {
    }
}