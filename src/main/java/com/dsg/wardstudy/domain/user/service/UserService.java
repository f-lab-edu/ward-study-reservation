package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.domain.user.dto.LoginDto;

public interface UserService {
    LoginDto signUp(SignUpRequest signUpDto);

    LoginDto getByEmailAndPassword(String email, String password);
}
