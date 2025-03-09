package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DbUserStorageTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private DbUserStorage dbUserStorage;

    @Test
    void saveUser_CallsRepositorySave() {
        User user = new User(null,"user@mail.com", "User");
        when(userRepository.save(any())).thenReturn(user);

        dbUserStorage.saveUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_CallsRepositoryDelete() {
        dbUserStorage.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}
