package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.messageManager.ErrorMessage;
import ru.yandex.practicum.filmorate.validator.ReleaseDate.ReleaseValidator;

import java.time.LocalDate;

@Data
public class Film extends Item {
    @NotBlank(message = ErrorMessage.FILM_EMPTY_NAME)
    private String name;
    @Size(max=200, message = ErrorMessage.FILM_MAX_DESCRIPTION)
    private String description;
    @ReleaseValidator(message = ErrorMessage.FILM_RELEASE_DATE)
    private LocalDate releaseDate;
    @Positive(message = ErrorMessage.FILM_DURATION)
    private int duration;
}