package ru.yandex.practicum.filmorate;

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
import ru.yandex.practicum.filmorate.controller.UserController;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    public static final String PATH = "/users";
    private UserController userController;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    void addUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(PATH).contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(
                "{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));

    }

    @Test
    void updateUserTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post(PATH).contentType(MediaType.APPLICATION_JSON).content())
    }



}
