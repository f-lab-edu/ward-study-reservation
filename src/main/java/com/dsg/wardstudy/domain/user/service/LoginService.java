package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.domain.user.dto.SignUpResponse;

import javax.servlet.http.HttpSession;

public interface LoginService {
    SignUpResponse signUp(SignUpRequest signUpDto, HttpSession session);

    void loginUser(LoginDto loginDto, HttpSession session);

    void logoutUser(HttpSession session);

}
