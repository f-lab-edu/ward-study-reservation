package com.dsg.wardstudy.service.user;

import com.dsg.wardstudy.dto.user.SignUpRequest;
import com.dsg.wardstudy.dto.user.LoginDto;

public interface UserService {
    LoginDto signUp(SignUpRequest signUpDto);

    LoginDto getByEmailAndPassword(String email, String password);
}
