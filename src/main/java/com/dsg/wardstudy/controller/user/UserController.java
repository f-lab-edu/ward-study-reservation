package com.dsg.wardstudy.controller.user;

import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.user.SignUpDto;
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
    public ResponseEntity<?> register(@RequestBody SignUpDto siginUpDto) {
        User user = User.builder()
                .name(siginUpDto.getName())
                .email(siginUpDto.getEmail())
                .nickname(siginUpDto.getNickname())
                .password(siginUpDto.getPassword())
                .build();

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
