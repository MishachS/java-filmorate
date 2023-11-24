package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Slf4j
@Service

public class UserService extends MainService<User> {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDBStorage") UserStorage userStorage, FriendStorage friendStorage) {
        super(userStorage);
        this.friendStorage = friendStorage;
        this.userStorage = userStorage;
    }

    public User addFriends(long idUser1, long idUser2) {
        User user = getId(idUser2);
        friendStorage.addFriend(idUser1, idUser2);
        return user;
    }

    public User deleteFriends(long idUser1, long idUser2) {
        User user = getId(idUser1);
        friendStorage.removeFriend(idUser1, idUser2);
        return user;
    }

    public List<User> commonFriend(long idUser1, long idUser2) {
        return friendStorage.getCommonFriends(idUser1, idUser2);
    }

    public List<User> allFriends(Long id) {
        return friendStorage.getFriends(id);
    }

    @Override
    public User create(User data) {
        validate(data);
        return userStorage.create(data);
    }

    @Override
    public User update(User data) {
        validate(data);
        return userStorage.update(data);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getId(Long id) {
        return userStorage.getId(id);
    }

    @Override
    public boolean delete(User data) {
        return userStorage.delete(data);
    }

    @Override
    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}