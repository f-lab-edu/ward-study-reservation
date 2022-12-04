package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.common.utils.Encryptor;
import com.dsg.wardstudy.domain.studyGroup.entity.StudyGroup;
import com.dsg.wardstudy.domain.user.entity.User;
import com.dsg.wardstudy.domain.user.entity.UserGroup;
import com.dsg.wardstudy.domain.user.dto.*;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.domain.user.constant.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserInfo create(SignUpRequest signUpRequest) {

        // pw 암호화해서 저장
        User savedUser = userRepository.save(User.of(signUpRequest));
        log.info("create savedUser: {}", savedUser);
        return UserInfo.mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getUser(Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("getById findUser: {}", findUser);
        return UserInfo.mapToDto(findUser);
    }


    @Override
    @Transactional(readOnly = true)
    public LoginDto getByEmailAndPassword(String email, String password) {
        // 파라미터 password가 origin
        User user = userRepository.findByEmail(email)
                .map(u -> Encryptor.isMatch(password, u.getPassword()) ? u : null)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("getByEmailAndPassword user : {}", user);

        return LoginDto.mapToDto(user);
    }

    @Override
    @Transactional
    public void withdrawUser(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("withdrawUser, findUser: {}", findUser);
        findUser.withdrawUser(true);
    }

}
