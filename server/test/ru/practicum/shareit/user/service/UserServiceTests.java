package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserService userService;

    private final User user = new User(1L, "User", "user@mail.ru");
    private final UserDTO userDTO = new UserDTO(1L, "User", "user@mail.ru");

    @Test
    void addUserWithUniqueEmailShouldReturnUserDTO() {
        UserRequestAddDto request = new UserRequestAddDto("User", "user@mail.ru");
        when(userStorage.saveUser(any(User.class))).thenReturn(user);
        when(userStorage.getUserByEmail(anyString())).thenReturn(null);
        UserDTO result = userService.addUser(request);
        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userStorage).saveUser(any(User.class));
    }

    @Test
    void addUserWithDuplicateEmailShouldThrowException() {
        UserRequestAddDto request = new UserRequestAddDto("User", "user@mail.ru");
        when(userStorage.getUserByEmail(anyString())).thenReturn(user);
        assertThrows(DataConflictException.class, () -> userService.addUser(request));
    }

    @Test
    void updateUserUpdateNameShouldReturnUpdatedUser() {
        UserRequestPatchDto request = new UserRequestPatchDto("NewUser", null);
        when(userStorage.getUser(1L)).thenReturn(user);
        when(userStorage.updateUser(any(User.class), anyLong())).thenReturn(user);
        UserDTO result = userService.updateUser(request, 1L);
        assertEquals("NewUser", result.getName());
        assertEquals("user@mail.ru", result.getEmail());
    }

    @Test
    void updateUserUpdateEmailToUniqueShouldReturnUpdatedUser() {
        UserRequestPatchDto request = new UserRequestPatchDto(null, "new@mail.ru");
        when(userStorage.getUser(1L)).thenReturn(user);
        when(userStorage.getUserByEmail("new@mail.ru")).thenReturn(null);
        when(userStorage.updateUser(any(User.class), anyLong())).thenReturn(user);
        UserDTO result = userService.updateUser(request, 1L);
        assertEquals("new@mail.ru", result.getEmail());
    }

    @Test
    void updateUserUpdateEmailToDuplicateShouldThrowException() {
        UserRequestPatchDto request = new UserRequestPatchDto(null, "duplicate@mail.ru");
        User existingUser = new User(2L, "Other", "duplicate@mail.ru");
        when(userStorage.getUser(1L)).thenReturn(user);
        when(userStorage.getUserByEmail("duplicate@mail.ru")).thenReturn(existingUser);
        assertThrows(DataConflictException.class, () -> userService.updateUser(request, 1L));
    }

    @Test
    void getUsersListShouldReturnListOfUsers() {
        when(userStorage.getUsers()).thenReturn(List.of(user));
        List<UserDTO> result = userService.getUsersList();
        assertEquals(1, result.size());
        assertEquals(userDTO.getEmail(), result.getFirst().getEmail());
        assertEquals(userDTO.getName(), result.getFirst().getName());
        assertEquals(userDTO.getId(), result.getFirst().getId());
    }

    @Test
    void getUserWithValidIdShouldReturnUser() {
        when(userStorage.getUser(1L)).thenReturn(user);
        UserDTO result = userService.getUser(1L);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void deleteUser_ShouldInvokeStorageDelete() {
        userService.deleteUser(1L);
        verify(userStorage).deleteUser(1L);
    }
}
