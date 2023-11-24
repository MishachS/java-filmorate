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
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FilmControllerTest {

    public static final String PATH = "/films";
    @Autowired
    private FilmController filmController;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addFilmTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("dataForTests/request/film")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addWrongFilmTest() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFile("dataForTests/request/film-wrong-releaseData")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void validateWrongFilmTest() {
        Film film = Film.builder()
                .name("filmTest")
                .description("filmTest")
                .releaseDate(LocalDate.of(1000, 11, 12))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    private String getContentFromFile(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return "Ошибка чтения файла!";
        }
    }
}
