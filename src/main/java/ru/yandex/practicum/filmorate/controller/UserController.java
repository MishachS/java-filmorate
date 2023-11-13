package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends MainController<User> {

    @GetMapping
    public List<User> getListUsers(){
        log.info("Get users");
        return getMap();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user){
        log.info("Add user{}", user);
        return create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user{}", user);
        return update(user);
    }


    @Override
    public void validate(User user){
        if(user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
    }
}
