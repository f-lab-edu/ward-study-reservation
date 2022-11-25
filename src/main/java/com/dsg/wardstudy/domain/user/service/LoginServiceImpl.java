package com.dsg.wardstudy.domain.user.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.dsg.wardstudy.domain.user.dto.SignUpResponse;
import com.dsg.wardstudy.domain.user.dto.UserInfo;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private static final String USER_ID = "USER_ID";
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest, HttpSession session) {

        // user 중복 체크
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent( u -> {
                    throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "duplicate UserGroup");
                });
        UserInfo userInfo = userService.create(signUpRequest);
        log.info("signUp userInfo : {}", userInfo);
        // session에 담고 리턴
        session.setAttribute(USER_ID, userInfo.getId());
        // password 암호화하고 저장
        return SignUpResponse.mapToDto(userInfo);

    }

    @Override
    @Transactional
    public void loginUser(LoginDto loginDto, HttpSession session) {
        // 세션 값이 있으면 리턴
        // 없으면 비밀번호 체크 후 로그인
        final Long userId = (Long) session.getAttribute(USER_ID);
        if (userId != null) {
            return;
        }
        LoginDto findLoginDto = userService.getByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (findLoginDto != null) {
            session.setAttribute(USER_ID, findLoginDto.getId());
        } else {
            throw new WSApiException(ErrorCode.NOT_FOUND_USER);
        }
    }

    @Override
    public void logoutUser(HttpSession session) {
        // 세션 제거
        session.removeAttribute(USER_ID);
    }
}
