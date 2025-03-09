package ru.practicum.shareit.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@EnableCaching
public class UserValidationTests {
    @MockBean
    UserClient userClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


    @Test
    void testAddUser() throws Exception {
        final UserDTO userDto = new UserDTO(1L, "John", "doe@tt.ru");
        Mockito.when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(userDto));
        mockMvc
                .perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("doe@tt.ru"));
    }

    @Test
    void testAddUserWithoutName() throws Exception {
        final UserDTO userDto = new UserDTO(1L, null, "doe@tt.ru");
        Mockito.when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(userDto));
        mockMvc
                .perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Поле name - обязательное"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void testAddUserWithoutEmail() throws Exception {
        final UserDTO userDto = new UserDTO(1L, "John", null);
        Mockito.when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(userDto));
        mockMvc
                .perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Поле email - обязательное"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void testAddUserWithIncorrectEmail() throws Exception {
        final UserDTO userDto = new UserDTO(1L, "John", "DoE");
        Mockito.when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(userDto));
        mockMvc
                .perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Некорректный формат электронной почты"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void testRouting() throws Exception {
        UserDTO validUser = new UserDTO(1L, "John", "john@example.com");

        mockMvc
                .perform(post("/users")
                        .content(mapper.writeValueAsString(validUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userClient).createUser(Mockito.any(UserRequestAddDto.class));
    }
}