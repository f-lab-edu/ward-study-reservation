package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.domain.user.dto.UserInfo;

public interface UserService {

    UserInfo create(SignUpRequest signUpRequest);

    UserInfo getUser(Long userId);

    LoginDto getByEmailAndPassword(String email, String password);

    UserGroup participate(Long studyGroupId, Long userId);

    void withdrawUser(Long userId);
}
