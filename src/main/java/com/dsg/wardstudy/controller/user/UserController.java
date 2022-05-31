package com.dsg.wardstudy.controller.user;

import com.dsg.wardstudy.dto.user.SignUpRequest;
import com.dsg.wardstudy.dto.user.UserDto;
import com.dsg.wardstudy.service.user.LoginService;
import com.dsg.wardstudy.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LoginService loginService;

    // 회원가입(register)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpDto) {

        log.info("users signup, signUpDto: {}", signUpDto);
        UserDto userDto = userService.signUp(signUpDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        UserDto findUserDto = userService.getByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

        loginService.loginUser(findUserDto.getId());

        return ResponseEntity.ok("login success!");
    }
}
