package com.dsg.wardstudy.service.user;

import com.dsg.wardstudy.dto.user.SignUpRequest;
import com.dsg.wardstudy.dto.user.UserDto;

public interface UserService {
    UserDto signUp(SignUpRequest signUpDto);

    UserDto getByEmailAndPassword(String email, String password);
}
