package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email(message = "Некорректный email.")
    private String email;
    @NotBlank(message = "Логин пользователя не может быть пустым.")
    private String login;
    private String name;
    @PastOrPresent(message = "День рождения не может быть в будущем.")
    private LocalDate birthday;
}