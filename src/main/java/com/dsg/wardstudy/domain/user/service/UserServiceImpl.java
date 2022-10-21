package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public LoginDto signUp(SignUpRequest signUpDto) {
        User user = SignUpRequest.mapToEntity(signUpDto);

        // user 중복 체크, 원할한 테스트를 위해 주석처리
/*        userRepository.findByEmail(user.getEmail())
                .ifPresent( u -> {
                    throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "duplicate UserGroup");
                });*/
        log.info("signUp user : {}", user);
        return LoginDto.mapToDto(userRepository.save(user));

    }

    @Override
    public LoginDto getByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("getByEmailAndPassword user : {}", user);

        return LoginDto.mapToDto(user);
    }

    @Override
    public UserGroup participate(Long userId, Long studyGroupId) {
        User participateUser = userRepository.findById(userId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("participate findById user : {}", participateUser);

        StudyGroup participateStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY));
        log.info("participate studyGroup : {}", participateStudyGroup);

        // 중복 등록 방지
        userGroupRepository.findByUserIdAndSGId(participateUser.getId(), participateStudyGroup.getId())
                .ifPresent(ug -> {
                    throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "duplicate UserGroup");
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
