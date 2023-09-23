package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @PostMapping
    ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(service.addUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> getUser(@PathVariable Long userId) {
        return new ResponseEntity<>(service.getUser(userId), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    ResponseEntity<User> patchUser(@PathVariable Long userId,
                                   @RequestBody User user) {
        return new ResponseEntity<>(service.patchUser(userId, user), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    HttpStatus deleteUser(@PathVariable("userId") Long userId) {
        service.deleteUser(userId);
        return HttpStatus.NO_CONTENT;
    }
}
