package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void testAddUser() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);
        System.out.println("sample = " + savedUser);
        assertNotNull(savedUser);
        assertEquals("John", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetAll() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setName("John");
        user1.setEmail("john@example.com");
        User user2 = new User();
        user2.setName("Jane");
        user2.setEmail("jane@example.com");
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAll();
        System.out.println("sample = " + userList);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("Jane", result.get(1).getName());
        assertEquals("jane@example.com", result.get(1).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserExistingIdShouldReturnUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUser(userId);
        System.out.println("sample = " + result);
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUserNonExistingIdShouldThrowException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUser(userId);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testPatchUserExistingIdShouldReturnUpdatedUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@example.com");

        User userPatch = new User();
        userPatch.setName("Jane");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.patchUser(userId, userPatch);
        System.out.println("sample = " + result);
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Jane", result.getName());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

}