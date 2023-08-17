package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User getUser(Long id);

    List<User> getAll();

    User patchUser(User user);

    void deleteUser(Long id);

    boolean contain(Long id);

}
