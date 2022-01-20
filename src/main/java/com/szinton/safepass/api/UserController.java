package com.szinton.safepass.api;

import com.szinton.safepass.dto.UserDto;
import com.szinton.safepass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Void> saveUser(@RequestBody @Valid UserDto userDto) {
        userService.saveUser(userDto);
        return new ResponseEntity<>(CREATED);
    }
}
