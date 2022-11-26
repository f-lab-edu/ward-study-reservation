package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.domain.user.dto.SignUpResponse;


public interface LoginService {
    SignUpResponse signUp(SignUpRequest signUpDto);

    void loginUser(LoginDto loginDto);

    void logoutUser();

    boolean isLoginUser();

}
