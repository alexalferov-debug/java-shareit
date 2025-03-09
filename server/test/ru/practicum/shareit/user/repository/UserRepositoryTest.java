package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailReturnsUser() {
        User savedUser = userRepository.save(new User(null, "Test", "test@mail.com"));
        User foundUser = userRepository.findUserByEmail("test@mail.com");
        assertThat(foundUser).isEqualTo(savedUser);
    }

    @Test
    void findByIdReturnsUser() {
        User user = userRepository.save(new User(null, "User", "user@mail.com"));
        User found = userRepository.findById(user.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("user@mail.com");
    }
}