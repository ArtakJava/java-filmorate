package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractCreatableStorage;

@Component
public class InMemoryUserCreatableStorage extends AbstractCreatableStorage<User> implements UserStorage {
}