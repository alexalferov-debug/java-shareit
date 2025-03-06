package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
@Primary
public class DbUserStorage implements UserStorage {

    private final UserRepository userRepository;

    @Autowired
    public DbUserStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, int userId) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
