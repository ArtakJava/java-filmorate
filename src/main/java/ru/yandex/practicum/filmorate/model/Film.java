package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.messageManager.ErrorMessage;
import ru.yandex.practicum.filmorate.validator.ReleaseDate.ReleaseValidator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends DataStorage {
    @NotBlank(message = ErrorMessage.FILM_EMPTY_NAME)
    private String name;
    @Size(max=200, message = ErrorMessage.FILM_MAX_DESCRIPTION)
    private String description;
    @ReleaseValidator(message = ErrorMessage.FILM_RELEASE_DATE)
    private LocalDate releaseDate;
    @Positive(message = ErrorMessage.FILM_DURATION)
    private int duration;
    @JsonIgnore
    private Set<Long> likesStorage = new HashSet<>();
    @JsonIgnore
    private int likes;

    public void addLike(Long id) {
        likesStorage.add(id);
        likes++;
    }

    public void removeLike(Long id) {
        likesStorage.remove(id);
        likes--;
    }
}