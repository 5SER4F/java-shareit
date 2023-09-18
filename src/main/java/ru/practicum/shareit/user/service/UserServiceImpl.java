package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.util.Patcher;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User getUser(Long id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(
                    String.format("Пользователь с id=%d не найден", id)
            );
        }
    }

    @Transactional
    @Override
    public User patchUser(Long id, User userPatch) {
        User oldUser = getUser(id);
        Patcher.setIfNotNull(oldUser::setName, userPatch.getName());
        Patcher.setIfNotNull(oldUser::setEmail, userPatch.getEmail());
        return userRepository.save(oldUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
