package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void addUser() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.addUser(user);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService, times(1)).addUser(user);
    }

    @Test
    void getUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@example.com");

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void getAll() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setName("John");
        user1.setEmail("john@example.com");
        User user2 = new User();
        user2.setName("Jane");
        user2.setEmail("jane@example.com");
        userList.add(user1);
        userList.add(user2);

        when(userService.getAll()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());

        verify(userService, times(1)).getAll();
    }

    @Test
    void patchUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@example.com");

        when(userService.patchUser(userId, user)).thenReturn(user);

        ResponseEntity<User> response = userController.patchUser(userId, user);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService, times(1)).patchUser(userId, user);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;

        HttpStatus response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response);

        verify(userService, times(1)).deleteUser(userId);
    }
}