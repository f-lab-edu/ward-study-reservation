package com.dsg.wardstudy.domain.user.controller;

import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.domain.user.service.LoginService;
import com.dsg.wardstudy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
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

    @PostMapping("/logout")
    public ResponseEntity<?> login() {
        loginService.logoutUser();

        return ResponseEntity.ok("logout success!");
    }

    // 일반유저가 스터디그룹에 참여
    @PostMapping("/{userId}/studyGroup/{studyGroupId}")
    public ResponseEntity<?> participate(
            @PathVariable("userId") Long userId,
            @PathVariable("studyGroupId") Long studyGroupId
    ) {
        log.info("users participate studyGroup, studyGroupId: {}, userId: {}", studyGroupId, userId);
        UserGroup participateUG = userService.participate(userId, studyGroupId);
        log.info("participateUG: {}", participateUG);

        return ResponseEntity.ok(participateUG);
    }
}
