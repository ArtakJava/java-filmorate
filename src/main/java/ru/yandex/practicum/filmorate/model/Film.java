package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseValidator;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max=200, message = "Количество символов в описании не может быть больше 200.")
    private String description;
    @ReleaseValidator(message = "Дата основания кино 28 декабря 1895 года. Дата выхода фильма должно быть позже.")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательным.")
    private int duration;
}