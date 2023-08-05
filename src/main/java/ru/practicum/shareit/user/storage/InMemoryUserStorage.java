package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.IllegalEmailException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Patcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();
    private long idCounter;

    @Override
    public User addUser(User user) {
        emailValidation(user.getEmail());
        user.setId(newId());
        put(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        if (id == null || !storage.containsKey(id)) {
            throw new ResourceNotFoundException("Пользователь не найден id=" + id);
        }
        return storage.get(id);
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public User patchUser(User userPatch) {
        User user = storage.get(userPatch.getId());
        if (userPatch.getEmail() != null && !userPatch.getEmail().equals(user.getEmail())) {
            emailValidation(userPatch.getEmail());
        }
        Patcher.setIfNotNull(user::setName, userPatch.getName());
        Patcher.setIfNotNull(user::setEmail, userPatch.getEmail());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        if (storage.remove(id) == null) {
            throw new ResourceNotFoundException("Пользователь не найден id=" + id);
        }
    }

    @Override
    public boolean contain(Long id) {
        return storage.containsKey(id);
    }

    private void put(User user) {
        storage.put(user.getId(), user);
    }

    private long newId() {
        return ++idCounter;
    }

    private void emailValidation(String email) {
        if (storage.values().stream().
                anyMatch(user -> user.getEmail().equals(email))
        ) {
            throw new IllegalEmailException("Email не является уникальным email=" + email);
        }
    }

}
