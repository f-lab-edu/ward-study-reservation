package com.dsg.wardstudy.domain.user.controller;

import com.dsg.wardstudy.config.auth.AuthUser;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.user.dto.*;
import com.dsg.wardstudy.domain.user.service.LoginService;
import com.dsg.wardstudy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignUpRequest signUpDto,
            HttpSession session
    ) {
        log.info("users signup, signUpDto: {}", signUpDto);
        SignUpResponse signUpResponse = loginService.signUp(signUpDto, session);

        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto,
            HttpSession session
    ) {
        loginService.loginUser(loginDto, session);

        return ResponseEntity.ok("login success!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> login(HttpSession session) {
        loginService.logoutUser(session);
        return ResponseEntity.ok("logout success!");
    }

    // 일반유저가 스터디그룹에 참여
    @PostMapping("/studyGroup/{studyGroupId}")
    public ResponseEntity<?> participate(
            @PathVariable("studyGroupId") Long studyGroupId,
            @AuthUser UserInfo userInfo
            ) {
        log.info("users participate studyGroup, " +
                "studyGroupId: {}, userInfo: {}", studyGroupId, userInfo);
        UserGroup participateUG = userService.participate(studyGroupId, userInfo);
        log.info("participateUG: {}", participateUG);

        return ResponseEntity.ok(participateUG);
    }
}
