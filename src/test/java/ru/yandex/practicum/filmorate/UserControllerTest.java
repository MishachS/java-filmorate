package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    public static final String PATH = "/users";
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringFromFile("dataForTests/request/user")))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void addUserWithWrongEmailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringFromFile("dataForTests/request/user-wrong-email")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void addUserWithWrongBirthDayTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringFromFile("dataForTests/request/user-wrong-birthday")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void validateUserTest() {
        User user = User.builder()
                .email("google@com")
                .login("Mishach")
                .birthday(LocalDate.of(2002, 11, 12))
                .build();
        User newUser = userController.addUser(user);
        Assertions.assertEquals(user.getName(), newUser.getName());
    }


    private String getStringFromFile(String fileName) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + fileName).toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Ошибка чтения файла!";
        }
    }



}