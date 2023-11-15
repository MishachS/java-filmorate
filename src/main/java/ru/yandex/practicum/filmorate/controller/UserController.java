package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends MainController<User> {

    private final UserService userService;

    @GetMapping
    public List<User> getListUsers() {
        log.info("Get users");
        return userService.getAll();
    }

    @GetMapping("{id}")
    public User getForId(@PathVariable Long id) {
        log.info("Get user id{}", id);
        return userService.getId(id);
    }

    @GetMapping("{id}/friends")
    public List<User> allFriendsForId(@PathVariable Long id) {
        log.info("Friends user{}", id);
        return userService.allFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Common friends user{}", id);
        return userService.commonFriend(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user{}", user);
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user{}", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Add friends{}", friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Delete friend{}", friendId);
        return userService.deleteFriends(id, friendId);
    }
}
