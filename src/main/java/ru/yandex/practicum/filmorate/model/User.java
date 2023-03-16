package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.messageManager.ErrorMessage;
import ru.yandex.practicum.filmorate.validator.Login.NotHaveSpace;

import javax.validation.constraints.*;
import java.sql.Date;

@Data
public class User extends DataStorage {
    @Email(message = ErrorMessage.USER_EMAIL)
    private String email;
    @NotBlank(message = ErrorMessage.USER_EMPTY_LOGIN)
    @NotHaveSpace(message = ErrorMessage.USER_LOGIN_SPACE)
    private String login;
    private String name;
    @PastOrPresent(message = ErrorMessage.USER_BIRTHDAY)
    private Date birthday;

    public User(Integer id, String email, String login, String name, Date birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {
    }
}