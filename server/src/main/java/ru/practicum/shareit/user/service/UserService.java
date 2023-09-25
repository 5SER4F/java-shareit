package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    List<User> getAll();

    User getUser(Long id);

    User patchUser(Long id, User user);

    void deleteUser(Long id);
}
