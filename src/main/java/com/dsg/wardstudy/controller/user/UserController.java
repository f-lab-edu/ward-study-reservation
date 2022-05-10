package com.dsg.wardstudy.controller.user;

import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.user.SignUpRequest;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpDto) {
        User user = User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(signUpDto.getPassword())
                .build();

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
