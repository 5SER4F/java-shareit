package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid User user) {
        return userClient.addUser(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@PathVariable Long userId,
                                            @RequestBody User user) {
        return userClient.patchUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public HttpStatus deleteUser(@PathVariable("userId") Long userId) {
        userClient.deleteUser(userId);
        return HttpStatus.NO_CONTENT;
    }
}
