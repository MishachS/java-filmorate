package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
@Deprecated
public class InMemoryUserStorage extends InMemoryMainStorage<User> implements UserStorage {
}
