package com.dsg.wardstudy.controller.user;

import com.dsg.wardstudy.dto.user.SignUpRequest;
import com.dsg.wardstudy.dto.user.LoginDto;
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

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LoginService loginService;

    // 회원가입(register)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpDto) {

        log.info("users signup, signUpDto: {}", signUpDto);
        LoginDto loginDto = userService.signUp(signUpDto);
        return new ResponseEntity<>(loginDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        LoginDto findUserDto = userService.getByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());

        loginService.loginUser(findUserDto.getId());

        return ResponseEntity.ok("login success!");
    }
}
