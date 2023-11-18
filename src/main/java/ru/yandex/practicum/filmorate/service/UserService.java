package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryMainStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends MainService<User> {
    public UserService(InMemoryMainStorage<User> inMemoryMainStorage) {
        super(inMemoryMainStorage);
    }

    public User addFriends(long idUser1, long idUser2) {
        User user1 = getId(idUser1);
        User user2 = getId(idUser2);
        user1.getFriends().add(idUser2);
        user2.getFriends().add(idUser1);
        return user2;
    }

    public User deleteFriends(long idUser1, long idUser2) {
        User user1 = getId(idUser1);
        User user2 = getId(idUser2);
        user1.getFriends().remove(idUser2);
        user2.getFriends().remove(idUser1);
        return user2;
    }

    public List<User> commonFriend(long idUser1, long idUser2) {
        List<User> list1 = allFriends(idUser1);
        List<User> longList = new ArrayList<>();
        if (list1.isEmpty()) {
            return longList;
        }
        List<User> list2 = allFriends(idUser2);
        if (list2.isEmpty()) {
            return longList;
        }
        list1.retainAll(list2);
        longList.addAll(list1);
        return longList;
    }

    public List<User> allFriends(Long id) {
        return getId(id)
                .getFriends()
                .stream()
                .map(friend -> getId(friend))
                .collect(Collectors.toList());

    }

    @Override
    public void validate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть равен NULL!");
        }
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
