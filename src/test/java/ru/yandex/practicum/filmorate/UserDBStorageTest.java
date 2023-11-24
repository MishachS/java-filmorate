package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.database.FriendDBStorage;
import ru.yandex.practicum.filmorate.storage.database.UserDBStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDBStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User newUser = User.builder()
                .id(1L)
                .email("sashamih12@yandex.ru")
                .login("mishach")
                .name("Alex")
                .birthday(LocalDate.of(2002, 11, 12))
                .build();
        UserDBStorage userStorage = new UserDBStorage(jdbcTemplate);
        userStorage.create(newUser);


        User savedUser = userStorage.getId(newUser.getId());


        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testFindUserAddFriend() {
        User newUser = User.builder()
                .id(1L)
                .email("sashamih12@yandex.ru")
                .login("mishach")
                .name("Alex")
                .birthday(LocalDate.of(2002, 11, 12))
                .build();
        UserDBStorage userStorage = new UserDBStorage(jdbcTemplate);
        FriendDBStorage friendStorage = new FriendDBStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userFriend = User.builder()
                .id(2L)
                .email("sashamih12Friend@yandex.ru")
                .login("mishachFriend")
                .name("Alex Friend")
                .birthday(LocalDate.of(1999, 4, 11))
                .build();
        userStorage.create(userFriend);
        friendStorage.addFriend(newUser.getId(), userFriend.getId());
        List<User> savedFriend = friendStorage.getFriends(newUser.getId());

        assertThat(savedFriend.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userFriend);
    }

    @Test
    public void testFindUserDeleteFriend() {
        User newUser = User.builder()
                .id(1L)
                .email("sashamih12@yandex.ru")
                .login("mishach")
                .name("Alex")
                .birthday(LocalDate.of(2002, 11, 12))
                .build();
        UserDBStorage userStorage = new UserDBStorage(jdbcTemplate);
        FriendDBStorage friendStorage = new FriendDBStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userFriend = User.builder()
                .id(2L)
                .email("sashamih12Friend@yandex.ru")
                .login("mishachFriend")
                .name("Alex Friend")
                .birthday(LocalDate.of(1999, 4, 11))
                .build();
        userStorage.create(userFriend);
        friendStorage.addFriend(newUser.getId(), userFriend.getId());
        friendStorage.removeFriend(newUser.getId(), userFriend.getId());
        List<User> savedFriend = friendStorage.getFriends(newUser.getId());

        assertThat(savedFriend.size())
                .isEqualTo(0);
    }

    @Test
    public void testFindUserCommonFriend() {
        User newUser = User.builder()
                .id(1L)
                .email("sashamih12@yandex.ru")
                .login("mishach")
                .name("Alex")
                .birthday(LocalDate.of(2002, 11, 12))
                .build();
        UserDBStorage userStorage = new UserDBStorage(jdbcTemplate);
        FriendDBStorage friendStorage = new FriendDBStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userCommonFriend = User.builder()
                .id(2L)
                .email("sashamih12CommonFriend@yandex.ru")
                .login("mishachCommonFriend")
                .name("Alex CommonFriend")
                .birthday(LocalDate.of(2000, 9, 15))
                .build();
        userStorage.create(userCommonFriend);
        User userFriend = User.builder()
                .id(2L)
                .email("sashamih12Friend@yandex.ru")
                .login("mishachFriend")
                .name("Alex Friend")
                .birthday(LocalDate.of(1999, 4, 11))
                .build();
        userStorage.create(userFriend);
        friendStorage.addFriend(newUser.getId(), userCommonFriend.getId());
        friendStorage.addFriend(userFriend.getId(), userCommonFriend.getId());

        List<User> savedFriend = friendStorage.getCommonFriends(newUser.getId(), userFriend.getId());

        assertThat(savedFriend.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userCommonFriend);
    }

    @Test
    public void testFindUserUpdate() {
        User newUser = User.builder()
                .id(1L)
                .email("sashamih12@yandex.ru")
                .login("mishach")
                .name("Alex")
                .birthday(LocalDate.of(2002, 11, 12))
                .build();
        UserDBStorage userStorage = new UserDBStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userUpdate = User.builder()
                .id(newUser.getId())
                .email("sashamih12Friend@yandex.ru")
                .login("mishachFriend")
                .name("Alex Friend")
                .birthday(LocalDate.of(1999, 4, 11))
                .build();
        userStorage.update(userUpdate);

        User saved = userStorage.getId(newUser.getId());

        assertThat(saved)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userUpdate);
    }
}