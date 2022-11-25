package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.common.utils.Encryptor;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.user.dto.*;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;


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
    public LoginDto getByEmailAndPassword(String email, String password) {
        // 파라미터 password가 hash값이다.
        User user = userRepository.findByEmail(email)
                .map(u -> Encryptor.isMatch(u.getPassword(), password) ? u : null)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("getByEmailAndPassword user : {}", user);

        return LoginDto.mapToDto(user);
    }

    @Override
    @Transactional
    public UserGroup participate(Long studyGroupId, UserInfo userInfo) {
        User participateUser = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("participate findById user : {}", participateUser);

        StudyGroup participateStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "studyGroup", studyGroupId));
        log.info("participate studyGroup : {}", participateStudyGroup);

        // 중복 등록 방지
        userGroupRepository.findByUserIdAndSGId(participateUser.getId(), participateStudyGroup.getId())
                .ifPresent(ug -> {
                    throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "UserGroup participateUserId: " +
                            participateUser.getId() + ", studyGroupId: " + participateStudyGroup.getId());
                });

        // studyGroup 등록시 UserType P(참여자)로 등록
        UserGroup userGroup = UserGroup.builder()
                .studyGroup(participateStudyGroup)
                .user(participateUser)
                .userType(UserType.PARTICIPANT)
                .build();

        return userGroupRepository.save(userGroup);

    }

}
