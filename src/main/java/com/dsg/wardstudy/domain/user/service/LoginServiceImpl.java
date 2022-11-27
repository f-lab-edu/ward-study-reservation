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
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private static final String USER_ID = "USER_ID";
    public final HttpSession httpSession;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        // user 중복 체크
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent( u -> {
                    throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "duplicate UserGroup");
                });
        UserInfo userInfo = userService.create(signUpRequest);
        log.info("signUp userInfo : {}", userInfo);
        // httpSession 에 담고 리턴
        httpSession.setAttribute(USER_ID, userInfo.getId());
        // password 암호화하고 저장
        return SignUpResponse.mapToDto(userInfo);

    }

    @Override
    public void loginUser(LoginDto loginDto) {
        // 세션 값(다른 사람의 세션, 원래 없어야 함!)체크, 있으면 리턴
        if (this.isLoginUser()) {
            return;
        }
        // 없으면 비밀번호 체크 후 로그인
        LoginDto findLoginDto = userService.getByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        log.info("loginUser findLoginDto: {}", findLoginDto);
        if (findLoginDto != null) {
            httpSession.setAttribute(USER_ID, findLoginDto.getId());
        } else {
            throw new WSApiException(ErrorCode.NOT_FOUND_USER);
        }
    }

    @Override
    public void logoutUser() {
        // 세션 제거
        httpSession.removeAttribute(USER_ID);
        log.info("session 제거");
    }

    /**
     * 로그인 확인 여부
     * @return 확인여부 boolean
     */
    @Override
    public boolean isLoginUser() {
        Long userId = (Long) httpSession.getAttribute(USER_ID);
        log.info("isLoginUser, userId: {}", userId);
        return userId != null;
    }

    @Override
    public Long getUserId() {
        Long userId = Optional.ofNullable(httpSession.getAttribute(USER_ID))
                .map(i -> (Long) i)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER));
        log.info("loginService getUserId userId: {}", userId);
        return userId;
    }
}
