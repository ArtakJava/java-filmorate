package ru.yandex.practicum.filmorate.errorMessage;

public class ErrorMessage {
    public static final String FILM_EMPTY_NAME = "Название фильма не может быть пустым.";
    public static final String FILM_MAX_DESCRIPTION = "Количество символов в описании не может быть больше 200.";
    public static final String FILM_RELEASE_DATE =
            "Дата основания кино 28 декабря 1895 года. Дата выхода фильма должно быть позже.";
    public static final String FILM_DURATION = "Продолжительность фильма не может быть отрицательным.";
    public static final String USER_EMAIL = "Некорректный email.";
    public static final String USER_EMPTY_LOGIN = "Логин пользователя не может быть пустым.";
    public static final String USER_LOGIN_SPACE = "Логин пользователя не может содержать пробелы.";
    public static final String USER_BIRTHDAY = "День рождения не может быть в будущем.";
    public static final String ITEM_NOT_EXIST = "Такого элемента нет.";
}