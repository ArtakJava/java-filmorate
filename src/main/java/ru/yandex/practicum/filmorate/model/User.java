package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.messageManager.ErrorMessage;
import ru.yandex.practicum.filmorate.validator.Login.NotHaveSpace;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User extends Item {
    @Email(message = ErrorMessage.USER_EMAIL)
    private String email;
    @NotBlank(message = ErrorMessage.USER_EMPTY_LOGIN)
    @NotHaveSpace(message = ErrorMessage.USER_LOGIN_SPACE)
    private String login;
    private String name;
    @PastOrPresent(message = ErrorMessage.USER_BIRTHDAY)
    private LocalDate birthday;
}