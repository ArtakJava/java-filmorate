package ru.yandex.practicum.filmorate.messageManager;

public class InfoMessage {
    public static final String GET_CREATE_REQUEST = "Получен запрос на создание: {}.";
    public static final String SUCCESS_CREATE = "Успешно создан: {}.";
    public static final String GET_UPDATE_REQUEST = "Получен запрос на обновление: {}.";
    public static final String SUCCESS_UPDATE = "Успешно обновлено: {}.";
    public static final String GET_ALL_REQUEST = "Получен запрос на получение всех данных.";
    public static final String SUCCESS_GET_ALL = "Успешно получены все данные.";
    public static final String GET_REQUEST = "Получен запрос на получение данных для ID = {}.";
    public static final String SUCCESS_GET = "Успешно получены данные ID = {}.";
    public static final String DATA_NOT_EXIST = "Нет данных.";
    public static final String ADD_FRIEND_REQUEST =
            "Запрос на добавление в друзья пользователей ID = {} и ID = {}.";
    public static final String SUCCESS_ADD_FRIEND = "Пользователи ID = {} и ID = {} добавили друг друга в друзья.";
    public static final String GET_FRIEND_LIST = "Запрос списка друзей пользователя с ID = {}.";
    public static final String SUCCESS_FRIEND_LIST = "Список друзей получен для пользователя с ID = {}.";
    public static final String GET_COMMON_FRIEND_LIST = "Запрос списка общих друзей пользователей ID = {} и ID = {}.";
    public static final String SUCCESS_COMMON_FRIEND_LIST =
            "Список общих друзей получен для пользователей ID = {} и ID = {}";
    public static final String GET_ADD_LIKE_REQUEST =
            "Запрос на добавление лайка пользователя ID = {} к фильму ID = {}.";
    public static final String SUCCESS_ADD_LIKE = "Успешно добавлен лайк пользователя ID = {} к фильму ID = {}.";
    public static final String GET_REMOVE_LIKE_REQUEST =
            "Запрос на удаление лайка пользователя ID = {} к фильму ID = {}";
    public static final String SUCCESS_REMOVE_LIKE = "Успешно удален лайк пользователя ID = {} к фильму ID = {}";
    public static final String GET_POPULAR_FILMS =
            "Запрос на получение {} популярных фильмов.";
    public static final String SUCCESS_POPULAR_FILMS = "Успешно получен список популярных фильмов в количестве: ";
}